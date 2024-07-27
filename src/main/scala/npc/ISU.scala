package npc

import chisel3._
import chisel3.util._

class Date_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
//AR
    val arvalid = Input(Bool())
    val araddr = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val arready = Output(Bool())
//R
    val rdata = Output(UInt(32.W))
    val rresp = Output(Bool())
    val rvalid = Output(Bool())
    val rready = Input(Bool())
//AW    
    val awvalid = Input(Bool())
    val awaddr = Input(UInt(32.W))
    val awready = Output(Bool())
//W
    val wvalid = Input(Bool())
    val wdata = Input(UInt(32.W))
    val len = Input(UInt(32.W))
    val wready = Output(Bool())
//B
    val bresp = Output(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
    
    
  })
  addResource("/Date_Memory.v")
}

class ISU extends Module{
    val io = IO(new Bundle{
        val in = Flipped(Decoupled(new EXU_ISU))
        val out = Decoupled(new ISU_WBU)
        val wbu_valid = Input(Bool())
    })

    val Dmem = Module(new Date_Memory())
    Dmem.io.clk := clock

    val finish_load = RegInit(false.B)
    
    val arvalid = RegInit(false.B) 
    val can_read = RegInit(false.B)
    val rready = true.B
    val awvalid = RegInit(false.B)
    val can_wirte = RegInit(false.B)
    val wvalid = RegInit(false.B)
    val bready = true.B

    when(io.in.bits.is_load){
        arvalid := true.B
    }
    
    when(arvalid && Dmem.io.arready){
        can_read := true.B
        arvalid := false.B
    }

    when(can_read && Dmem.io.rvalid && rready){
        finish_load := true.B
        can_read := false.B
    }.elsewhen(finish_load){
        finish_load := false.B
    }

    when(io.in.bits.mem_wr_en){
        awvalid := true.B
    }

    when(awvalid && Dmem.io.awready){   //写地址握手成功
        awvalid := false.B
        wvalid := true.B
    }

    when(wvalid && Dmem.io.wready){     //写数据握手成功
        can_wirte := true.B
        wvalid := false.B
    }

    when(Dmem.io.bresp){        //写数据成功
        can_wirte := false.B
    }

//Dmem
    Dmem.io.len := io.in.bits.len
    Dmem.io.load_unsign := io.in.bits.load_unsign
    Dmem.io.araddr := io.in.bits.alu_out
    Dmem.io.awaddr := io.in.bits.alu_out
    Dmem.io.wdata := io.in.bits.data
    
    Dmem.io.awvalid := awvalid
    Dmem.io.arvalid := arvalid
    Dmem.io.wvalid := wvalid
    Dmem.io.rready := rready
    Dmem.io.bready := bready

//for wbu
    io.out.bits.dm_out := Dmem.io.rdata
    io.out.bits.alu_out := io.in.bits.alu_out
    io.out.bits.jump_jalr := io.in.bits.jump_jalr
    io.out.bits.jump_en := io.in.bits.jump_en
    io.out.bits.imm := io.in.bits.imm
    io.out.bits.is_ecall := io.in.bits.is_ecall
    io.out.bits.is_mret := io.in.bits.is_mret
    io.out.bits.mtvec := io.in.bits.mtvec
    io.out.bits.epc := io.in.bits.epc
    io.out.bits.rd1 := io.in.bits.rd1
    io.out.bits.mem_rd_en := io.in.bits.mem_rd_en
    io.out.bits.mem_wr_en := io.in.bits.mem_wr_en
    io.out.bits.rf_wr_en := io.in.bits.rf_wr_en
    io.out.bits.finish_load := finish_load
    io.out.bits.is_csr := io.in.bits.is_csr
    
//for wbu
    io.out.bits.is_cmp := io.in.bits.is_cmp
    io.out.bits.is_load := io.in.bits.is_load
    io.out.bits.isS_type := io.in.bits.isS_type
    io.out.bits.can_wirte := can_wirte

    io.out.valid := io.in.valid
    io.in.ready := io.out.ready
}
