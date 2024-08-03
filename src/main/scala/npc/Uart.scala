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
    val mtime     = RegInit(0.U(64.W))
  
  // Default values for outputs

    val arready = RegInit(true.B)
    val rresp   = RegInit(false.B)
    val rvalid  = RegInit(false.B)
    val awready = RegInit(false.B)
    val wready  = RegInit(false.B)
    val bresp   = RegInit(false.B)
    val bvalid  = RegInit(false.B)
    val rdata   = RegInit(0.U(32.W))

    io.out.bits.arready := arready
    io.out.bits.rresp   := rresp
    io.out.bits.rvalid  := rvalid
    io.out.bits.bresp   := bresp
    io.out.bits.bvalid  := bvalid
    io.out.bits.wready  := wready
    io.out.bits.awready := awready
    io.out.bits.rdata   := rdata
    deviceReg           := io.in.bits.wdata

    io.out.bits.rlast := false.B
    io.out.bits.bid   := 0.U    
    
  // Increment mtime
    mtime := mtime + 1.U

  // Read data handshake
    when(io.out.bits.arready && io.in.bits.arvalid) {
        rvalid := true.B
        when(io.in.bits.araddr < "h8000_0000".U || io.in.bits.araddr > "h87ff_ffff".U){
            when(io.in.bits.araddr === "ha000_0048".U){
                rresp := true.B
                rdata := mtime(31, 0)
                arready := false.B
            }.elsewhen(io.in.bits.araddr === "ha000_004c".U){
                rresp := true.B
                rdata := mtime(63, 32)
                arready := false.B
            }
        }.otherwise{
            rresp := false.B
        }
    }

  // Reset read handshake signals after data is accepted
    when(rvalid && io.in.bits.rready) {
        rvalid := false.B
        rresp  := false.B
        arready := true.B
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
            printf("%c", deviceReg(7, 0))      //仅仅是调试语句
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

    io.in.ready  := io.out.valid
    io.out.valid := true.B
}
