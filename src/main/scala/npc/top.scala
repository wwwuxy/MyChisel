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

    })

    val ifu = Module(new IFU)
    val idu = Module(new IDU)
    val exu = Module(new EXU)
    val pc = Module(new PC)

    StageConnect(ifu.io.out, idu.io.in)
    StageConnect(idu.io.out, exu.io.in)

    ifu.io.out.ready := true.B
    idu.io.in.valid := true.B
    idu.io.out.ready := true.B
    exu.io.in.valid := true.B

    ifu.io.pc := pc.io.next_pc

    pc.io.jump_jalr := idu.io.jump_jalr
    pc.io.jump_en := idu.io.jump_en
    pc.io.imm := idu.io.imm
    pc.io.is_ecall := idu.io.is_ecall
    pc.io.mtvec := idu.io.mtvec
    pc.io.rd1 := idu.io.rd1
    pc.io.is_mret := idu.io.is_mret
    pc.io.epc := idu.io.epc
    idu.io.alu_rsl := exu.io.alu_rsl
    
    
    


//for sdb    
    io.pc := ifu.io.out.bits.pc
    io.alu_rsl := exu.io.alu_rsl
    io.inst := ifu.io.out.bits.inst

}

object StageConnect {
  def apply[T <: Data](left: DecoupledIO[T], right: DecoupledIO[T]) = {
    val arch = "single"
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


