package npc
import chisel3._
import chisel3.util._

class SRAM extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
  //AR
    val arvalid     = Input(Bool())
    val araddr      = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val arready     = Output(Bool())
  //R
    val rdata  = Output(UInt(32.W))
    val rresp  = Output(Bool())
    val rvalid = Output(Bool())
    val rready = Input(Bool())
  //AW    
    val awvalid = Input(Bool())
    val awaddr  = Input(UInt(32.W))
    val awready = Output(Bool())
  //W
    val wvalid = Input(Bool())
    val wdata  = Input(UInt(32.W))
    val len    = Input(UInt(32.W))
    val wready = Output(Bool())
  //B
    val bresp  = Output(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
  })
  addResource("/SRAM.v")
}




//for yosys

// class Date_Sramory extends Module {
//   val io = IO(new Bundle {
//     val clk = Input(Clock())
//     //AR
//     val arvalid = Input(Bool())
//     val araddr = Input(UInt(32.W))
//     val load_unsign = Input(Bool())
//     val arready = Output(Bool())
//     //R
//     val rdata = Output(UInt(32.W))
//     val rresp = Output(Bool())
//     val rvalid = Output(Bool())
//     val rready = Input(Bool())
//     //AW    
//     val awvalid = Input(Bool())
//     val awaddr = Input(UInt(32.W))
//     val awready = Output(Bool())
//     //W
//     val wvalid = Input(Bool())
//     val wdata = Input(UInt(32.W))
//     val len = Input(UInt(32.W))
//     val wready = Output(Bool())
//     //B
//     val bresp = Output(Bool())
//     val bvalid = Output(Bool())
//     val bready = Input(Bool())
//   })

//   // Internal Sramory
//   val Sram = Sram(10240, UInt(8.W))

//   // Default output values
//   io.arready := true.B
//   io.awready := true.B
//   io.wready  := true.B

//   io.rresp := 0.U
//   io.rdata := 0.U
//   io.bresp := 0.U

//   val rvalidReg = RegInit(false.B)
//   val bvalidReg = RegInit(false.B)

//   io.rvalid := rvalidReg
//   io.bvalid := bvalidReg

//   // Read data channel
//   when(io.arvalid && io.arready && !rvalidReg) {
//     val readAddr = io.araddr(31, 2)
//     io.rdata := Cat(
//       Sram(readAddr + 3.U),
//       Sram(readAddr + 2.U),
//       Sram(readAddr + 1.U),
//       Sram(readAddr)
//     )
//     rvalidReg := true.B
//     io.rresp := 0.U // OKAY response
//   }

//   // Ensure rvalid is set for only one cycle
//   when(io.rvalid && io.rready) {
//     rvalidReg := false.B
//   }

//   // Write response channel
//   when(io.awvalid && io.awready && io.wvalid && io.wready && !bvalidReg) {
//     val writeAddr = io.awaddr(31, 2)
//     when(io.len(0)) { Sram(writeAddr) := io.wdata(7, 0) }
//     when(io.len(1)) { Sram(writeAddr + 1.U) := io.wdata(15, 8) }
//     when(io.len(2)) { Sram(writeAddr + 2.U) := io.wdata(23, 16) }
//     when(io.len(3)) { Sram(writeAddr + 3.U) := io.wdata(31, 24) }

//     bvalidReg := true.B
//     io.bresp := 0.U // OKAY response
//   }

//   // Ensure bvalid is set for only one cycle
//   when(io.bvalid && io.bready) {
//     bvalidReg := false.B
//   }
// }

