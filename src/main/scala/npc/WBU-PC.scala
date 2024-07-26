package npc

import chisel3._
import chisel3.util._

class WBU_PC extends Bundle{
    val jump_jalr = Bool()
    val jump_en = Bool()
    val imm = UInt(32.W)
    val is_ecall = Bool()
    val is_mret = Bool()
    val mtvec = UInt(32.W)
    val epc = UInt(32.W)
    val rd1 = UInt(32.W)     //for jalr
}