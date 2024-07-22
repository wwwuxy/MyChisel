// package npc

// import chisel3._
// import chisel3.util._

// class inputreg extends Module{
//     val io =IO(new Bundle {
//         // val alu_out = Input(UInt(32.W))
//         val dm_out = Input(UInt(32.W))
//         val rf_wr_sel = Input(UInt(3.W))
//         val storepc = Input(UInt(32.W))
//         val wd = Output(UInt(32.W))
//     })

//     val wd = VecInit(Seq(io.dm_out, io.dm_out, io.storepc))

//     io.wd := Mux1H(io.rf_wr_sel, wd)
// }

// // object inputreg extends App{
// //     emitVerilog(new inputreg(), Array("--target-dir","generated"))
// // }
