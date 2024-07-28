package npc

import chisel3._
import chisel3.util._

class EXU extends Module{
    val io = IO(new Bundle {
        val in = Flipped(Decoupled(new IDU_EXU))
        val out = Decoupled(new EXU_ISU)
        val alu_rsl = Output(UInt(32.W))
    })

        val Alu = Module(new ALU)
        Alu.io.pc := io.in.bits.pc
        Alu.io.rs1 := io.in.bits.rs1
        Alu.io.rs2 := io.in.bits.rs2
        Alu.io.imm := io.in.bits.imm
        Alu.io.alu_a_sel := io.in.bits.alu_a_sel
        Alu.io.alu_b_sel := io.in.bits.alu_b_sel
        Alu.io.alu_sel := io.in.bits.alu_sel

        io.alu_rsl := Alu.io.rsl
//for isu
        io.out.bits.data := io.in.bits.rs2
        io.out.bits.alu_out := io.alu_rsl
        io.out.bits.mem_wr_en := io.in.bits.mem_wr_en
        io.out.bits.mem_rd_en := io.in.bits.mem_rd_en
        io.out.bits.rf_wr_en := io.in.bits.rf_wr_en
        io.out.bits.len := io.in.bits.len
        io.out.bits.load_unsign := io.in.bits.load_unsign

//for pc
        io.out.bits.imm := io.in.bits.imm
        io.out.bits.jump_en := io.in.bits.jump_en
        io.out.bits.jump_jalr := io.in.bits.jump_jalr
        io.out.bits.is_ecall := io.in.bits.is_ecall
        io.out.bits.is_mret := io.in.bits.is_mret
        io.out.bits.is_csr := io.in.bits.is_csr
        io.out.bits.mtvec := io.in.bits.mtvec
        io.out.bits.epc := io.in.bits.epc
        io.out.bits.rd1 := io.in.bits.rs1

//for isu
        io.out.bits.is_load := io.in.bits.is_load
        io.out.bits.isS_type := io.in.bits.isS_type
        io.out.bits.is_j := io.in.bits.is_j
        io.out.bits.pc := io.in.bits.pc
//for wbu
        io.out.bits.is_cmp := io.in.bits.is_cmp
        

  // State Machine
        val sIdle :: sValid :: Nil = Enum(2)
        val state = RegInit(sIdle)    
        switch(state) {
          is(sIdle) {
            when(io.in.valid) {
              state := sValid
            }
          }
          is(sValid) {
              state := sIdle
            }
          }   
        io.in.ready := (state === sIdle)
        io.out.valid := (state === sValid)
}
