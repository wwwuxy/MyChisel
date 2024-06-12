import chisel3._
import chisel3.util._


class shift extends Module{
    val  io = IO(new Bundle {
        val res = Output(UInt(8.W))
    })
    val ShiftReg = Reg(UInt(8.W))
    ShiftReg := "b0000_0001".U
    // val tmp = ShiftReg(0) ^ ShiftReg(2) ^ ShiftReg(3) ^ ShiftReg(4)
    ShiftReg := Cat(ShiftReg(7, 1), ShiftReg(0) ^ ShiftReg(2) ^ ShiftReg(3) ^ ShiftReg(4))
    io.res := ShiftReg
}

object shift extends App{
    emitVerilog(new shift(), Array("--target-dir","generated"))
}