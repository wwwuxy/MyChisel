package npc

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.util.Decoupled
import chisel3.util.Fill
import chisel3.util.Cat
import java.lang.ModuleLayer.Controller
import javax.sound.sampled.Control


class IDU extends Module{
    val io = IO(new Bundle{
        val in = Flipped(Decoupled(new IFU_IDU))
        val out = Decoupled(new IDU_EXU)
// for write back
        val alu_rsl = Input(UInt(32.W))   
        val dm_out = Input(UInt(32.W))
        val wbu_valid = Input(Bool())
    })

    val Contorller = Module(new CONTORLLER())
    val RegisterFile = Module(new REGISTERFILE())

//Controller
        Contorller.io.inst := io.in.bits.inst
        Contorller.io.rs1 := RegisterFile.io.rd1
        Contorller.io.rs2 := RegisterFile.io.rd2
        io.out.bits.alu_sel := Contorller.io.alu_sel
        io.out.bits.alu_a_sel := Contorller.io.alu_a_sel
        io.out.bits.alu_b_sel := Contorller.io.alu_b_sel
        io.out.bits.pc := io.in.bits.pc
        io.out.bits.len := Contorller.io.len

//RegisterFile
        RegisterFile.io.inst := io.in.bits.inst
        RegisterFile.io.wr_en := Contorller.io.rf_wr_en && io.wbu_valid
        RegisterFile.io.dm_out := io.dm_out
        RegisterFile.io.alu_out := io.alu_rsl
        RegisterFile.io.rf_wr_sel := Contorller.io.rf_wr_sel
        RegisterFile.io.is_csr := Contorller.io.is_csr
        RegisterFile.io.is_ecall := Contorller.io.is_ecall
        RegisterFile.io.pc := io.in.bits.pc
        RegisterFile.io.is_mret := Contorller.io.is_mret
        io.out.bits.rs1 := RegisterFile.io.rd1
        io.out.bits.rs2 := RegisterFile.io.rd2
        io.out.bits.imm := Contorller.io.imm

//for pc
        io.out.bits.jump_jalr := Contorller.io.jump_jalr
        io.out.bits.jump_en := Contorller.io.jump_en
        io.out.bits.imm := Contorller.io.imm
        io.out.bits.is_ecall := Contorller.io.is_ecall
        io.out.bits.is_mret := Contorller.io.is_mret
        io.out.bits.mtvec := RegisterFile.io.mtvec
        io.out.bits.epc := RegisterFile.io.epc

//for isu
        io.out.bits.mem_rd_en := Contorller.io.mem_rd_en
        io.out.bits.mem_wr_en := Contorller.io.mem_wr_en
        io.out.bits.load_unsign := Contorller.io.load_unsign
        io.out.bits.rf_wr_en := Contorller.io.rf_wr_en
        io.out.bits.is_csr := Contorller.io.is_csr
//for wbu
        io.out.bits.is_cmp := Contorller.io.is_cmp

    io.in.ready := io.out.ready
    io.out.valid := io.in.valid
}


