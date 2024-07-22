package npctest

import chisel3._
import chisel3.util._

class Zongxian extends Bundle{
    val inst = UInt(32.W)
}

class  IFU extends Module{
    val io = IO(new Bundle {
        val out = Decoupled(new Zongxian)
    })

    val s_idle :: s_wait_ready :: Nil = Enum(2)
    val state = RegInit(s_idle)
    
    state := MuxLookup(state, s_idle)(List(
        s_idle        -> Mux(io.out.ready, s_wait_ready, s_idle),
        s_wait_ready  -> Mux(io.out.valid, s_idle, s_wait_ready)
    ))
}

class IDU extends Module{
    val io = IO(new Bundle {
        val in = Flipped(Decoupled(new Zongxian))
    })

    val s_wait_vaild :: s_ready :: Nil = Enum(2)
    val state = RegInit(s_wait_vaild)

    state := MuxLookup(state, s_wait_vaild)(List(
        s_wait_vaild -> Mux(io.in.valid, s_ready, s_wait_vaild),
        s_ready      -> Mux(io.in.ready, s_wait_vaild, s_ready)
    ))

    val inst = RegInit(0.U(32.W))
    when(state === s_ready){
        inst := io.in.bits.inst
    }
}