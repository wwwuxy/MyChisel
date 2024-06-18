package npc

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile
import chisel3.util.MemoryFile


class mem extends Module{
    val io = IO(new Bundle {
        val im_addr = Input(UInt(32.W))
        val dm_addr = Input(UInt(32.W))
        val dm_in = Input(UInt(32.W))
        val mem_wr = Input(Bool())
        val im_out = Output(UInt(32.W))
        val dm_out = Output(UInt(32.W))
    })
    val mem = SyncReadMem(1024, UInt(32.W))     //4kb

    io.im_out := 0.U
    io.dm_out := 0.U
    loadMemoryFromFile(mem, "mem.hex")      //读入内存数据
    
    when(io.mem_wr){
        mem(io.dm_addr) := io.dm_in
    }.otherwise{
        io.im_out := mem(io.im_addr)
        io.dm_out := mem(io.dm_addr)
    }
}


// object mem extends App{
//     emitVerilog(new mem(), Array("--target-dir", "generated"))
// }