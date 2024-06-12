package npc

import chisel3._
import chisel3.util._

class registerfile extends Module{
    val io = IO(new Bundle {
        val pc = Input(UInt(32.W))
        val wren = Input(Bool())
        val rsl = Input(UInt(32.W))
        val rs1 = Output(UInt(32.W))
        val rs2 = Output(UInt(32.W))
        val inst = Output(UInt(32.W))
    })
//创建一个包含32个32位宽的序列，初始化为0，将该序列转换为一个矢量，再存储
    val FileReg = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
    val rd_addr = Wire(UInt(5.W))
    rd_addr := io.pc(11,7)

    FileReg(0) := 0.U

    io.rs1 := FileReg(io.pc(19, 15))
    io.rs2 := FileReg(io.pc(24, 20))
    io.inst := io.pc

    when(io.wren){
        FileReg(rd_addr) := io.rsl
    }
// FileReg(rd_addr) := Mux(io.wren, io.rsl, FileReg(rd_addr))
    
}

// object registerfile extends App{
//     emitVerilog(new registerfile(), Array("--target-dir", "generated"))
// }
