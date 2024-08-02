package npc

import chisel3._
import chisel3.stage._
import circt.stage.ChiselStage
import chisel3.util._

class top extends Module{
    val io = IO(new Bundle {
        val pc        = Output(UInt(32.W))
        val alu_rsl   = Output(UInt(32.W))
        val inst      = Output(UInt(32.W))
        val imm       = Output(UInt(32.W))
        val diff_test = Output(Bool())
        val wbu_valid = Output(Bool())
    })

    val ifu = Module(new IFU)
    val idu = Module(new IDU)
    val exu = Module(new EXU)
    val isu = Module(new ISU)
    val wbu = Module(new WBU)
    val pc  = Module(new PC)
    val xbar = Module(new XBAR)

    StageConnect(ifu.io.out, idu.io.in)
    StageConnect(ifu.io.ifu_axi_out, xbar.io.ifu_axi_in)
    StageConnect(ifu.io.ifu_axi_in, xbar.io.ifu_axi_out)
    StageConnect(idu.io.out, exu.io.in)
    StageConnect(exu.io.out, isu.io.in)
    StageConnect(isu.io.out, wbu.io.in)
    StageConnect(isu.io.isu_axi_out, xbar.io.isu_axi_in)
    StageConnect(isu.io.isu_axi_in, xbar.io.isu_axi_out)   
    StageConnect(wbu.io.out, pc.io.in)
  
    ifu.io.pc := pc.io.next_pc
    
    idu.io.alu_rsl   := wbu.io.alu_out
    idu.io.dm_out    := wbu.io.dm_out
    idu.io.wbu_valid := wbu.io.wbu_valid
    isu.io.wbu_valid := wbu.io.wbu_valid

//AXI仲裁器
    xbar.io.ifu_valid := ifu.io.ifu_valid
    xbar.io.isu_valid := isu.io.isu_valid

//for sdb    
    io.pc := idu.io.out.bits.pc
    io.alu_rsl := exu.io.alu_rsl
    io.inst := ifu.io.out.bits.inst
    io.imm := idu.io.out.bits.imm
    io.diff_test := ifu.io.diff_test
    io.wbu_valid := wbu.io.wbu_valid
}

object StageConnect {
  def apply[T <: Data](left: DecoupledIO[T], right: DecoupledIO[T]) = {
    val arch = "multi"
    if      (arch == "single")   { right.bits := left.bits }
    else if (arch == "multi")    { right <> left }
    // else if (arch == "pipeline") { right <> RegEnable(left, left.fire) }
    // else if (arch == "ooo")      { right <> Queue(left, 16) }
  }
}

