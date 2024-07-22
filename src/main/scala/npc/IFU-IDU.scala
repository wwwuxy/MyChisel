package npc

import chisel3._
import chisel3.util._

class IFU_IDU extends Bundle{
    val inst = Output(UInt(32.W))
    val pc = Output(UInt(32.W)) 
} 
