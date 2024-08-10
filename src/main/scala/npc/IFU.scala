package npc

import chisel3._
import chisel3.util._


class IFU extends  Module{
    val io = IO(new Bundle {
        val pc          = Input(UInt(32.W))
        val out         = Decoupled(new IFU_IDU)
        val ifu_axi_out = Decoupled(new TO_AXI)
        val ifu_axi_in  = Flipped(Decoupled(new FROM_AXI))
        val ifu_valid   = Output(Bool())
        val diff_test   = Output(Bool())
    })
    io.diff_test := false.B
//set signs
    val arvalid = RegInit(false.B)
    val valid   = RegInit(false.B)
//initial
    io.ifu_axi_out.bits.load_unsign := false.B
    io.ifu_axi_out.bits.arvalid     := arvalid
    io.ifu_axi_out.bits.rready      := true.B
    io.ifu_axi_out.bits.awaddr      := 0.U
    io.ifu_axi_out.bits.wdata       := 0.U
    io.ifu_axi_out.bits.wstrb       := 0.U
    io.ifu_axi_out.bits.awvalid     := false.B
    io.ifu_axi_out.bits.bready      := true.B
    io.ifu_axi_out.bits.wvalid      := false.B
    io.ifu_axi_out.bits.araddr      := io.pc
    io.ifu_axi_out.bits.arsize      := 4.U

    io.ifu_axi_out.bits.awid        := 0.U
    io.ifu_axi_out.bits.arid        := 0.U
    io.ifu_axi_out.bits.arlen       := 0.U
    io.ifu_axi_out.bits.awlen       := 0.U
    io.ifu_axi_out.bits.awsize      := 0.U
    io.ifu_axi_out.bits.awburst     := 0.U
    io.ifu_axi_out.bits.arburst     := 0.U
    io.ifu_axi_out.bits.wlast       := false.B
    

    io.ifu_valid                    := valid

    val IPC = RegInit(0.U(32.W))
    val IR  = RegInit(0.U(32.W))

    val sIdle :: sFetch :: sGetinst :: sValid :: Nil = Enum(4)
    val state = RegInit(sIdle)

    switch(state) {
      is(sIdle) {
        when(io.pc =/= IPC) {
            valid := true.B
            when(io.ifu_axi_out.ready){  //AXI ready，可以与SRAM通信
                arvalid      := true.B
                io.diff_test := true.B
                state        := sFetch
            }
        }
      }
      is(sFetch) {
        when(io.ifu_axi_out.bits.arvalid && io.ifu_axi_in.bits.arready) {
          arvalid := false.B
          state   := sGetinst
        }
      }
      is(sGetinst) {
        when(io.ifu_axi_out.bits.rready && io.ifu_axi_in.bits.rvalid) {
          when(io.ifu_axi_in.bits.rresp === 0.U) {
            IR    := io.ifu_axi_in.bits.rdata
            IPC   := io.pc
            valid := false.B
            state := sValid
          }
        }
      }
      is(sValid){
          when(io.out.ready){
              state := sIdle
          }
      }
    }

    io.ifu_axi_out.valid := valid
    io.ifu_axi_in.ready  := valid
    
    io.out.valid     := (state === sValid)
    io.out.bits.pc   := IPC
    io.out.bits.inst := IR
}   
