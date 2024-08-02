package npc

import chisel3._
import chisel3.util._

class UART extends Module {
  val io = IO(new Bundle {
    val in  = Flipped(Decoupled(new TO_AXI))
    val out = Decoupled(new FROM_AXI)
})
  
// Device register to store data
    val deviceReg = RegInit(0.U(32.W))
  
// Default values for outputs
    io.out.bits.arready := true.B
    io.out.bits.rdata := 0.U

    val rresp   = RegInit(false.B)
    val rvalid  = RegInit(false.B)
    val awready = RegInit(false.B)
    val wready  = RegInit(false.B)
    val bresp   = RegInit(false.B)
    val bvalid  = RegInit(false.B)

    io.out.bits.rresp   := rresp
    io.out.bits.rvalid  := rvalid
    io.out.bits.bresp   := bresp
    io.out.bits.bvalid  := bvalid
    io.out.bits.wready  := wready
    io.out.bits.awready := awready
  
// Read data handshake
    when(io.out.bits.arready && io.in.bits.arvalid) {
        io.out.bits.rdata  := deviceReg
        io.out.bits.rvalid := true.B
        when(io.in.bits.araddr < "h8000_0000".U || io.in.bits.araddr > "h87ff_ffff".U){
            io.out.bits.rresp := true.B
        }.otherwise{
            io.out.bits.rresp := false.B
        }
    }

// Reset read handshake signals after data is accepted
    when(rvalid && io.in.bits.rready) {
        rvalid := false.B
    }

// Write address handshake
    when(io.in.bits.awvalid && !awready) {
        awready := true.B
    }
  
// Write data handshake
    when(io.in.bits.wvalid && !wready) {
        wready := true.B
    }
  
// Write response handshake
    when(wready && io.in.bits.wvalid) {
        when(io.in.bits.awaddr === "ha000_03f8".U){   //串口输出
            deviceReg := io.in.bits.wdata
            printf("%c", deviceReg(7, 0))
            bvalid := true.B
            bresp  := true.B
        }.otherwise{
            bvalid := true.B
            bresp  := false.B
        }
    }
        
// Reset write handshake signals after response is accepted
    when(bvalid && io.in.bits.bready) {
        wready := false.B
        bvalid := false.B
    }

    when(bresp && !bvalid){
        bresp := false.B
    }

    io.in.ready := true.B
    io.out.valid := true.B
}
