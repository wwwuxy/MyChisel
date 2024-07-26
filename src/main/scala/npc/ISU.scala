package npc

import chisel3._
import chisel3.util._

class Date_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val alu_out = Input(UInt(32.W))
    val data = Input(UInt(32.W))
    val wr_en = Input(Bool())
    val rd_en = Input(Bool())
    val len = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val dm_out = Output(UInt(32.W))
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

    val dm_reg = RegInit(0.U(32.W))
    val finish_load = RegInit(false.B)
    val next_pc = RegInit(true.B)
    dm_reg := Dmem.io.dm_out

    when(io.in.bits.mem_rd_en && !finish_load){
        finish_load := true.B
    }.elsewhen(finish_load){
        finish_load := false.B
        next_pc := true.B
    }

    when(next_pc){
        finish_load := false.B
        next_pc := false.B
    }
//Dmem
    Dmem.io.alu_out := io.in.bits.alu_out
    Dmem.io.data := io.in.bits.data
    Dmem.io.wr_en := io.in.bits.mem_wr_en 
    Dmem.io.rd_en := io.in.bits.mem_rd_en && !finish_load && !next_pc
    Dmem.io.len := io.in.bits.len
    Dmem.io.load_unsign := io.in.bits.load_unsign

//for wbu
    io.out.bits.dm_out := dm_reg
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

    io.out.valid := io.in.valid   //false表示没有访问内存
    io.in.ready := io.out.ready
}
