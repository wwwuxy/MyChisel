package npctest

import chisel3._
import chisel3.util._

class registerfile1 extends Module{
    val io = IO(new Bundle {
        val inst = Input(UInt(32.W))
        val wr_en = Input(Bool())
        val wd = Input(UInt(32.W))
        val rd1 = Output(UInt(32.W))
        val rd2 = Output(UInt(32.W))
    })

//inital
    val rs1 = io.inst(19, 15)
    val rs2 = io.inst(24, 20)
    val rd = io.inst(11, 7)
//创建一个包含32个32位宽的序列，初始化为0，将该序列转换为一个矢量，再存储
    val FileReg = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))    
    FileReg(0) := 0.U

    io.rd1 := FileReg(rs1)
    io.rd2 := FileReg(rs2)

    FileReg(rd) := Mux(io.wr_en, io.wd, FileReg(rd))

}

// object registerfile extends App{
//     emitVerilog(new registerfile(), Array("--target-dir", "generated"))
// }
