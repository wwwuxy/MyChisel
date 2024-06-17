package lab

import chisel3._


class count10 extends Module{
    val io = IO(new Bundle {
        val dout = Output(UInt(32.W))
    })

    val add = Module(new adder())
    val reg = Module(new  register())

    val count = reg.io.q

    add.io.a := 1.U
    add.io.b := count
    val res = add.io.y

    val next = Mux(res === 9.U, 0.U, res + 1.U)

    reg.io.d := next
    io.dout := count
   
}

object count10 extends App{
    emitVerilog(new count10(), Array("--target-dir", "generated"))
}