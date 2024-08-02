package  npc

import chisel3._
import chisel3.util._


class XBAR extends Module{
    val io = IO(new Bundle{
        val ifu_axi_in  = Flipped(Decoupled(new TO_AXI))
        val ifu_axi_out = Decoupled(new FROM_AXI)
        val isu_axi_in  = Flipped(Decoupled(new TO_AXI))
        val isu_axi_out = Decoupled(new FROM_AXI)
        val ifu_valid   = Input(Bool())
        val isu_valid   = Input(Bool())
    })

    val Sram = Module(new SRAM())
    val Uart = Module(new UART())

    Sram.io.clk := clock

    val sIdle :: sIFU :: sISU :: sHandshake :: sBusy :: Nil = Enum(5)
    val state                                               = RegInit(sIdle)

    switch(state) {
      is(sIdle) {
        when(io.ifu_valid) {
          state := sIFU
        }.elsewhen(io.isu_valid) {
          state := sISU
        }
      }
      is(sIFU) {
        when(io.ifu_axi_in.valid && io.ifu_axi_in.bits.arvalid) {
          state := sHandshake
        }
      }
      is(sISU) {
        when(io.isu_axi_in.valid && (io.isu_axi_in.bits.arvalid || io.isu_axi_in.bits.awvalid)) {
          state := sHandshake
        }
      }
      is(sHandshake) {
        when(io.ifu_axi_out.ready || io.isu_axi_out.ready) {
          state := sBusy
        }
      }
      is(sBusy) {
        when(Sram.io.rresp || Sram.io.bresp) {
          state := sIdle
        }
      }
    }

  val ifu_selected = (state === sIFU) || (state === sBusy && io.ifu_valid)
  val isu_selected = (state === sISU) || (state === sBusy && io.isu_valid)

  val uart_selected = RegInit(false.B)

  when(ifu_selected && (io.ifu_axi_in.bits.araddr < "h8000_0000".U || io.ifu_axi_in.bits.araddr > "h87ff_ffff".U)) {
    uart_selected := true.B
  }.elsewhen(isu_selected && (io.isu_axi_in.bits.araddr < "h8000_0000".U || io.isu_axi_in.bits.araddr > "h87ff_ffff".U)) {
    uart_selected := true.B
  }.otherwise{
    uart_selected := false.B
  }

  Sram.io.arvalid             := Mux(uart_selected, false.B, Mux(ifu_selected, io.ifu_axi_in.bits.arvalid, io.isu_axi_in.bits.arvalid))
  Uart.io.in.bits.arvalid     := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.arvalid, io.isu_axi_in.bits.arvalid), false.B)
  io.ifu_axi_out.bits.arready := Mux(uart_selected, Uart.io.out.bits.arready, Sram.io.arready) && (state === sBusy)
  io.isu_axi_out.bits.arready := Mux(uart_selected, Uart.io.out.bits.arready, Sram.io.arready) && (state === sBusy)
  Sram.io.araddr              := Mux(uart_selected, 0.U, Mux(ifu_selected, io.ifu_axi_in.bits.araddr, io.isu_axi_in.bits.araddr))
  Uart.io.in.bits.araddr      := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.araddr, io.isu_axi_in.bits.araddr), 0.U)
  Sram.io.load_unsign         := Mux(uart_selected, false.B, Mux(ifu_selected, io.ifu_axi_in.bits.load_unsign, io.isu_axi_in.bits.load_unsign))
  Uart.io.in.bits.load_unsign := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.load_unsign, io.isu_axi_in.bits.load_unsign), false.B)

  io.ifu_axi_out.bits.rvalid := Mux(uart_selected, Uart.io.out.bits.rvalid, Sram.io.rvalid) && (state === sBusy)
  io.isu_axi_out.bits.rvalid := Mux(uart_selected, Uart.io.out.bits.rvalid, Sram.io.rvalid) && (state === sBusy)
  Sram.io.rready             := Mux(uart_selected, false.B, Mux(ifu_selected, io.ifu_axi_in.bits.rready, io.isu_axi_in.bits.rready)) && (state === sBusy)
  Uart.io.in.bits.rready     := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.rready, io.isu_axi_in.bits.rready), false.B)
  io.ifu_axi_out.bits.rdata  := Mux(uart_selected, Uart.io.out.bits.rdata, Sram.io.rdata)
  io.isu_axi_out.bits.rdata  := Mux(uart_selected, Uart.io.out.bits.rdata, Sram.io.rdata)
  io.ifu_axi_out.bits.rresp  := Mux(uart_selected, Uart.io.out.bits.rresp, Sram.io.rresp) && (state === sBusy)
  io.isu_axi_out.bits.rresp  := Mux(uart_selected, Uart.io.out.bits.rresp, Sram.io.rresp) && (state === sBusy)

  Sram.io.awvalid             := Mux(uart_selected, false.B, Mux(ifu_selected, io.ifu_axi_in.bits.awvalid, io.isu_axi_in.bits.awvalid))
  Uart.io.in.bits.awvalid     := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.awvalid, io.isu_axi_in.bits.awvalid), false.B)
  io.ifu_axi_out.bits.awready := Mux(uart_selected, Uart.io.out.bits.awready, Sram.io.awready) && (state === sBusy)
  io.isu_axi_out.bits.awready := Mux(uart_selected, Uart.io.out.bits.awready, Sram.io.awready) && (state === sBusy)
  Sram.io.awaddr              := Mux(uart_selected, 0.U, Mux(ifu_selected, io.ifu_axi_in.bits.awaddr, io.isu_axi_in.bits.awaddr))
  Uart.io.in.bits.awaddr      := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.awaddr, io.isu_axi_in.bits.awaddr), 0.U)

  Sram.io.wvalid             := Mux(uart_selected, false.B, Mux(ifu_selected, io.ifu_axi_in.bits.wvalid, io.isu_axi_in.bits.wvalid)) && (state === sBusy)
  Uart.io.in.bits.wvalid     := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.wvalid, io.isu_axi_in.bits.wvalid), false.B)
  io.ifu_axi_out.bits.wready := Mux(uart_selected, Uart.io.out.bits.wready, Sram.io.wready) && (state === sBusy)
  io.isu_axi_out.bits.wready := Mux(uart_selected, Uart.io.out.bits.wready, Sram.io.wready) && (state === sBusy)
  Sram.io.wdata              := Mux(uart_selected, 0.U, Mux(ifu_selected, io.ifu_axi_in.bits.wdata, io.isu_axi_in.bits.wdata))
  Uart.io.in.bits.wdata      := Mux(uart_selected , Mux(ifu_selected, io.ifu_axi_in.bits.wdata, io.isu_axi_in.bits.wdata), 0.U)
  Sram.io.len                := Mux(uart_selected, 0.U, Mux(ifu_selected, io.ifu_axi_in.bits.wstrb, io.isu_axi_in.bits.wstrb))
  Uart.io.in.bits.wstrb      := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.wstrb, io.isu_axi_in.bits.wstrb), 0.U)

  io.ifu_axi_out.bits.bvalid := Mux(uart_selected, Uart.io.out.bits.bvalid, Sram.io.bvalid) && (state === sBusy)
  io.isu_axi_out.bits.bvalid := Mux(uart_selected, Uart.io.out.bits.bvalid, Sram.io.bvalid) && (state === sBusy)
  Sram.io.bready             := Mux(uart_selected, false.B, Mux(ifu_selected, io.ifu_axi_in.bits.bready, io.isu_axi_in.bits.bready)) && (state === sBusy)
  Uart.io.in.bits.bready     := Mux(uart_selected, Mux(ifu_selected, io.ifu_axi_in.bits.bready, io.isu_axi_in.bits.bready), false.B)
  io.ifu_axi_out.bits.bresp  := Mux(uart_selected, Uart.io.out.bits.bresp, Sram.io.bresp) && (state === sBusy)
  io.isu_axi_out.bits.bresp  := Mux(uart_selected, Uart.io.out.bits.bresp, Sram.io.bresp) && (state === sBusy)

  Uart.io.in.valid     := true.B
  Uart.io.out.ready    := true.B
  io.ifu_axi_in.ready  := ifu_selected
  io.isu_axi_in.ready  := isu_selected
  io.ifu_axi_out.valid := (state === sBusy && ifu_selected)
  io.isu_axi_out.valid := (state === sBusy && isu_selected)
}



    //ifu、isu不会同时请求通信，故谁来了用谁
    //   Sram.io.arvalid             := Mux(ifu_selected, io.ifu_axi_in.bits.arvalid, io.isu_axi_in.bits.arvalid)
    //   io.ifu_axi_out.bits.arready := Mux(ifu_selected, Sram.io.arready, false.B) && (state === sBusy)
    //   io.isu_axi_out.bits.arready := Mux(isu_selected, Sram.io.arready, false.B) && (state === sBusy)
    //   Sram.io.araddr              := Mux(ifu_selected, io.ifu_axi_in.bits.araddr, io.isu_axi_in.bits.araddr)
    //   Sram.io.load_unsign         := Mux(ifu_selected, io.ifu_axi_in.bits.load_unsign, io.isu_axi_in.bits.load_unsign)

    //   io.ifu_axi_out.bits.rvalid := Mux(ifu_selected, Sram.io.rvalid, false.B) && (state === sBusy)
    //   io.isu_axi_out.bits.rvalid := Mux(isu_selected, Sram.io.rvalid, false.B) && (state === sBusy)
    //   Sram.io.rready             := Mux(ifu_selected, io.ifu_axi_in.bits.rready, io.isu_axi_in.bits.rready) && (state === sBusy)
    //   io.ifu_axi_out.bits.rdata  := Sram.io.rdata
    //   io.isu_axi_out.bits.rdata  := Sram.io.rdata
    //   io.ifu_axi_out.bits.rresp  := Mux(ifu_selected, Sram.io.rresp, false.B) && (state === sBusy)
    //   io.isu_axi_out.bits.rresp  := Mux(isu_selected, Sram.io.rresp, false.B) && (state === sBusy)

    //   Sram.io.awvalid             := Mux(ifu_selected, io.ifu_axi_in.bits.awvalid, io.isu_axi_in.bits.awvalid)
    //   io.ifu_axi_out.bits.awready := Mux(ifu_selected, Sram.io.awready, false.B) && (state === sBusy)
    //   io.isu_axi_out.bits.awready := Mux(isu_selected, Sram.io.awready, false.B) && (state === sBusy)
    //   Sram.io.awaddr              := Mux(ifu_selected, io.ifu_axi_in.bits.awaddr, io.isu_axi_in.bits.awaddr)

    //   Sram.io.wvalid             := Mux(ifu_selected, io.ifu_axi_in.bits.wvalid, io.isu_axi_in.bits.wvalid) && (state === sBusy)
    //   io.ifu_axi_out.bits.wready := Mux(ifu_selected, Sram.io.wready, false.B) && (state === sBusy)
    //   io.isu_axi_out.bits.wready := Mux(isu_selected, Sram.io.wready, false.B) && (state === sBusy)
    //   Sram.io.wdata              := Mux(ifu_selected, io.ifu_axi_in.bits.wdata, io.isu_axi_in.bits.wdata)
    //   Sram.io.len                := Mux(ifu_selected, io.ifu_axi_in.bits.wstrb, io.isu_axi_in.bits.wstrb)

    //   io.ifu_axi_out.bits.bvalid := Mux(ifu_selected, Sram.io.bvalid, false.B) && (state === sBusy)
    //   io.isu_axi_out.bits.bvalid := Mux(isu_selected, Sram.io.bvalid, false.B) && (state === sBusy)
    //   Sram.io.bready             := Mux(ifu_selected, io.ifu_axi_in.bits.bready, io.isu_axi_in.bits.bready) && (state === sBusy)
    //   io.ifu_axi_out.bits.bresp  := Mux(ifu_selected, Sram.io.bresp, false.B) && (state === sBusy)
    //   io.isu_axi_out.bits.bresp  := Mux(isu_selected, Sram.io.bresp, false.B) && (state === sBusy)

    //   io.ifu_axi_in.ready  := ifu_selected
    //   io.isu_axi_in.ready  := isu_selected
    //   io.ifu_axi_out.valid := (state  === sBusy && ifu_selected)
    //   io.isu_axi_out.valid := (state  === sBusy && isu_selected)
    // }
