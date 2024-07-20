package npc

import chisel3._
import chisel3.util._

class registerfile extends Module{
    val io = IO(new Bundle {
        val inst = Input(UInt(32.W))
        val wr_en = Input(Bool())
        val wd = Input(UInt(32.W))
        val is_csr = Input(Bool())
        val is_ecall = Input(Bool())
        val pc = Input(UInt(32.W))
        val rd1 = Output(UInt(32.W))
        val rd2 = Output(UInt(32.W))
    })

//inital
    val rs1 = io.inst(19, 15)
    val rs2 = io.inst(24, 20)
    val rd = io.inst(11, 7)
    io.rd1 := 0.U
    io.rd2 := 0.U

//for csr指令    
    val fun3 = io.inst(14, 12)   
    val csr = io.inst(31, 20)   //选择csr寄存器
    val csr_sel = Wire(UInt(2.W))
    csr_sel := 0.U      //inital
    when(csr === 0x0300.U){      //mstatus
        csr_sel := 0.U
    }.elsewhen(csr === 0x0305.U){    //mtvec
        csr_sel := 1.U
    }.elsewhen(csr === 0x0341.U){    //mepc
        csr_sel := 2.U
    }.elsewhen(csr === 0x0342.U){    //mcause
        csr_sel := 3.U
    }

//创建一个包含32个32位宽的序列，初始化为0，将该序列转换为一个矢量，再存储
    val FileReg = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))    
    val CsrReg = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))
    val t = RegInit(0.U(32.W))
    CsrReg(0) := "h00001800".U  //for difftest

    when(io.is_ecall){
            CsrReg(2) := io.pc
            CsrReg(3) := 11.U
            io.rd2 := CsrReg(1)
    }

    when(io.is_csr){
        when(fun3 === "b001".U){   //csrrw
            t := CsrReg(csr_sel)
            CsrReg(csr_sel) := FileReg(rs1)
            FileReg(rd) := t
        }.elsewhen(fun3 === "b010".U){   //csrrs
            t := CsrReg(csr_sel)
            CsrReg(csr_sel) := t | FileReg(rs1)
            FileReg(rd) := t
        }.elsewhen(fun3 === "b011".U){   //csrrc
            t := CsrReg(csr_sel)
            CsrReg(csr_sel) := t & (~FileReg(rs1))
            FileReg(rd) := t
        }.otherwise{    //无操作，暂定初始化mstatus
            CsrReg(0) := "h00001800".U
        }
    }.otherwise{
        io.rd1 := FileReg(rs1)
        io.rd2 := FileReg(rs2)
        FileReg(rd) := Mux(io.wr_en, io.wd, FileReg(rd))
        FileReg(0) := 0.U   
    }
}
// object registerfile extends App{
//     emitVerilog(new registerfile(), Array("--target-dir", "generated"))
// }
