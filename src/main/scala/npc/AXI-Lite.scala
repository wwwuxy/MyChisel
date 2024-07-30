package npc

import chisel3._
import chisel3.util._

class  TO_AXI extends Bundle{
    val arvalid     = Output(Bool())
    val load_unsign = Output(Bool())
    val araddr      = Output(UInt(32.W))
    val rready      = Output(Bool())
    val awvalid     = Output(Bool())
    val awaddr      = Output(UInt(32.W))
    val wvalid      = Output(Bool())
    val wdata       = Output(UInt(32.W))
    val wstrb       = Output(UInt(32.W))
    val bready      = Output(Bool())
}

class  FROM_AXI extends Bundle{
    val arready = Output(Bool())
    val rvalid  = Output(Bool())
    val rdata   = Output(UInt(32.W))
    val rresp   = Output(Bool())
    val awready = Output(Bool())
    val wready  = Output(Bool())
    val bvalid  = Output(Bool())
    val bresp   = Output(Bool())
}

