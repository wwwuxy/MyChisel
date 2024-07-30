package npc

import chisel3._
import chisel3.util._

class REGISTERFILE extends Module{
    val io = IO(new Bundle {
        val inst      = Input(UInt(32.W))
        val wr_en     = Input(Bool())
        val dm_out    = Input(UInt(32.W))
        val alu_out   = Input(UInt(32.W))
        val rf_wr_sel = Input(UInt(3.W))
        val is_csr    = Input(Bool())
        val is_ecall  = Input(Bool())
        val is_mret   = Input(Bool())
        val pc        = Input(UInt(32.W))
        val rd1       = Output(UInt(32.W))
        val rd2       = Output(UInt(32.W))
        val mtvec     = Output(UInt(32.W))
        val epc       = Output(UInt(32.W))
    })

//inital
    val rs1     = io.inst(19, 15)
    val rs2     = io.inst(24, 20)
    val rd      = io.inst(11, 7)
        io.rd1 := 0.U
        io.rd2 := 0.U

    val storepc = io.pc + 4.U
    val rf_wr   = VecInit(Seq(io.alu_out, io.dm_out, storepc))
    val wd      = Mux1H(io.rf_wr_sel, rf_wr)

//for csr指令    
    val fun3    = io.inst(14, 12)
    val csr     = io.inst(31, 20)  //选择csr寄存器
    val csr_sel = Wire(UInt(2.W))

    csr_sel := 0.U      //inital
    when(csr === "h300".U){      //mstatus
        csr_sel := 0.U
    }.elsewhen(csr === "h305".U){    //mtvec
        csr_sel := 1.U
    }.elsewhen(csr === "h341".U){    //mepc
        csr_sel := 2.U
    }.elsewhen(csr === "h342".U){    //mcause
        csr_sel := 3.U
    }

//创建一个包含32个32位宽的序列，初始化为0，将该序列转换为一个矢量，再存储
    val FileReg = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
    val CsrReg  = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))
    val t       = RegInit(0.U(32.W))

    CsrReg(0)  := "h00001800".U  //for difftest
    io.mtvec   := 0.U
    io.epc     := 0.U
    FileReg(0) := 0.U


    when(io.is_ecall){
            val MIE = CsrReg(0)(3)
            CsrReg(0)   := CsrReg(0) & "hfffffffb".U
            CsrReg(0)   := CsrReg(0) | (MIE << 7)
            CsrReg(0)   := CsrReg(0) & "hfffffffe".U
            CsrReg(2.U) := io.pc
            CsrReg(3.U) := 11.U
            io.mtvec    := CsrReg(1.U)
    }.elsewhen(io.is_mret){
            val MPIE = CsrReg(0)(7)
            CsrReg(0) := CsrReg(0) & "hfffffffb".U
            CsrReg(0) := CsrReg(0) | (MPIE << 3)
            CsrReg(0) := CsrReg(0) | "h00000080".U
            io.epc    := CsrReg(2.U)
    }.elsewhen(io.is_csr){
            when(fun3 === "b001".U){   //csrrw
                FileReg(rd)     := CsrReg(csr_sel)
                CsrReg(csr_sel) := FileReg(rs1)
            
            }.elsewhen(fun3 === "b010".U){   //csrrs
                FileReg(rd)     := CsrReg(csr_sel)
                CsrReg(csr_sel) := CsrReg(csr_sel) | FileReg(rs1)
            
            }.elsewhen(fun3 === "b011".U){   //csrrc
                FileReg(rd)     := CsrReg(csr_sel)
                CsrReg(csr_sel) := CsrReg(csr_sel) & (~FileReg(rs1))
                
            }.otherwise{    //无操作，暂定初始化mstatus
                CsrReg(0) := "h00001800".U
            }
    }.otherwise{
        io.rd1      := FileReg(rs1)
        io.rd2      := FileReg(rs2)
        FileReg(rd) := Mux(io.wr_en, wd, FileReg(rd))
        FileReg(0)  := 0.U
    }
    FileReg(0) := 0.U
}
