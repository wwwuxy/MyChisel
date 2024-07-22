package npc

import chisel3._
import chisel3.util._

class IDU_EXU extends Bundle{
    val rs1 = Output(UInt(32.W))
    val rs2 = Output(UInt(32.W))
    val imm = Output(UInt(32.W))
    val alu_sel = Output(UInt(13.W))
    val alu_a_sel = Output(Bool())
    val alu_b_sel = Output(Bool()) 
    val pc = Output(UInt(32.W))
} 
