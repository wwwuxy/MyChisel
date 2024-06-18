package npc

import chisel3._

class inputreg extends Module{
    val io =IO(new Bundle {
        val alu_out = Input(UInt(32.W))
        val dm_out = Input(UInt(32.W))
        val rf_wr_sel = Input(Bool())
        val wd = Output(UInt(32.W))
    })

    io.wd := Mux(io.rf_wr_sel, io.alu_out, io.dm_out)
}

object inputreg extends App{
    emitVerilog(new inputreg(), Array("--target-dir","generated"))
}
