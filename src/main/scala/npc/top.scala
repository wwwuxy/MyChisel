package npc

import chisel3._
import chisel3.stage._
import circt.stage.ChiselStage
import chisel3.util._

class top extends Module{
    val io = IO(new Bundle {
        val pc = Output(UInt(32.W))
        val alu_rsl = Output(UInt(32.W))
        val inst =  Output(UInt(32.W))
        val imm = Output(UInt(32.W))
        val diff_test = Output(Bool())
        val wbu_valid = Output(Bool())
    })

    val ifu = Module(new IFU)
    val idu = Module(new IDU)
    val exu = Module(new EXU)
    val isu = Module(new ISU)
    val wbu = Module(new WBU)
    val pc = Module(new PC)

    StageConnect(ifu.io.out, idu.io.in)
    StageConnect(idu.io.out, exu.io.in)
    StageConnect(exu.io.out, isu.io.in)
    StageConnect(isu.io.out, wbu.io.in)
    StageConnect(wbu.io.out, pc.io.in)

    
    ifu.io.pc := pc.io.next_pc
    // pc.io.no_ld := idu.io.out.valid

    idu.io.alu_rsl := wbu.io.alu_out
    idu.io.dm_out := wbu.io.dm_out 
    idu.io.wbu_valid := wbu.io.wbu_valid
    pc.io.wbu_valid := wbu.io.wbu_valid
    isu.io.wbu_valid := wbu.io.wbu_valid

    
    
    


//for sdb    
    io.pc := exu.io.in.bits.pc
    io.alu_rsl := exu.io.alu_rsl
    io.inst := ifu.io.out.bits.inst
    io.imm := idu.io.out.bits.imm
    io.diff_test := pc.io.diff_test
    io.wbu_valid := wbu.io.wbu_valid
}

object StageConnect {
  def apply[T <: Data](left: DecoupledIO[T], right: DecoupledIO[T]) = {
    val arch = "multi"
    if      (arch == "single")   { right.bits := left.bits }
    else if (arch == "multi")    { right <> left }
    else if (arch == "pipeline") { right <> RegEnable(left, left.fire) }
    else if (arch == "ooo")      { right <> Queue(left, 16) }
  }
}



object top extends App{
    var filltlflag = Array[String]()
    filltlflag = filltlflag ++ Array(
        "--target-dir", "generated",
        "--target:verilog",
        // "--split-verilog",
        // "--lowering-options=" + Seq(
        //     "disallowLocalVariables",
        //     "disallowPackedArrays"
        // ).mkString(","),
        // "--disable-all-randomization"
        )

    ChiselStage.emitSystemVerilogFile(
        new top,
        filltlflag
    )
}


