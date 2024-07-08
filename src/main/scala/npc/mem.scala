// package npc

// import chisel3._
// import chisel3.util._
// import chisel3.experimental._
// class Memory extends BlackBox with HasBlackBoxResource {
//   val io = IO(new Bundle {
//     val clk = Input(Clock())
//     val pc = Input(UInt(32.W))
//     val data_addr = Input(UInt(32.W))
//     val data = Input(UInt(32.W))   
//     val wr_en = Input(Bool())
//     val rd_en = Input(Bool())
//     val inst = Output(UInt(32.W))
//   })

//   addResource("/Memory.v")
// }


// class MEM extends Module{
// //     val io = IO(new Bundle {
// //         val clk = Input(Clock())
// //         val pc = Input(UInt(32.W))
// //         val data = Input(UInt(32.W))
// //         val data_addr = Input(UInt(32.W))
// //         val wr_en = Input(Bool())
// //         val rd_en = Input(Bool())
// //         val inst = Output(UInt(32.W))   
// //     })
//     val mem = Module(new Memory)
//     // mem.io.clk := clock
//     // mem.io.pc := io.pc
//     // mem.io.data := io.data
//     // mem.io.data_addr := io.data_addr
//     // mem.io.wr_en := io.wr_en
//     // mem.io.rd_en := io.rd_en
//     // io.inst := mem.io.inst
// }


// // object MEM extends App{
// //     emitVerilog(new MEM(), Array("--target-dir", "generated"))
// // }