package npc

import chisel3._
import chisel3.util._


class ISU extends Module {
  val io = IO(new Bundle {
    val in          = Flipped(Decoupled(new EXU_ISU))
    val out         = Decoupled(new ISU_WBU)
    val isu_axi_out = Decoupled(new TO_AXI)
    val isu_axi_in  = Flipped(Decoupled(new FROM_AXI))
    val isu_valid   = Output(Bool())
    val wbu_valid   = Input(Bool())
  })

  val store_finish = RegInit(false.B)
  val load_finish  = RegInit(false.B)
  val arvalid      = RegInit(false.B)
  val awvalid      = RegInit(false.B)
  val wvalid       = RegInit(false.B)
  val valid        = RegInit(false.B)
  val dm_out       = RegInit(0.U(32.W))

  io.isu_valid := valid

  // Initializing isu_axi_out signals
  io.isu_axi_out.bits.arvalid := arvalid
  io.isu_axi_out.bits.rready  := true.B
  io.isu_axi_out.bits.awvalid := awvalid
  io.isu_axi_out.bits.wvalid  := wvalid
  io.isu_axi_out.bits.bready  := true.B

  io.isu_axi_out.bits.awid    := 0.U
  io.isu_axi_out.bits.arid    := 0.U
  io.isu_axi_out.bits.arlen   := 0.U
  io.isu_axi_out.bits.awlen   := 0.U
  io.isu_axi_out.bits.awburst := 0.U
  io.isu_axi_out.bits.arburst := 0.U
  io.isu_axi_out.bits.wlast   := true.B
  
  io.isu_axi_out.valid := false.B
  io.isu_axi_in.ready  := false.B

  io.isu_axi_out.bits.arsize      := io.in.bits.arsize
  io.isu_axi_out.bits.awsize      := io.in.bits.awsize
  io.isu_axi_out.bits.wstrb       := io.in.bits.wstrb
  io.isu_axi_out.bits.araddr      := io.in.bits.alu_out
  io.isu_axi_out.bits.awaddr      := io.in.bits.alu_out
  io.isu_axi_out.bits.wdata       := io.in.bits.data

  when(io.in.bits.load_unsign){
    when(io.in.bits.arsize === 0.U){
      dm_out := Cat(Fill(24, 0.U), io.isu_axi_in.bits.rdata(7, 0))
    }.elsewhen(io.in.bits.arsize === 1.U){
      dm_out := Cat(Fill(16, 0.U), io.isu_axi_in.bits.rdata(15, 0))
    }.otherwise{
      dm_out := io.isu_axi_in.bits.rdata
    }
  }
    

  // State Machine for load/store operations
  val sIdle :: sHandshake :: sLoad :: sStoreAddr :: sStoreData :: sValid :: Nil = Enum(6)
  
  val state = RegInit(sIdle)

  switch(state) {
    is(sIdle) {
      when(io.in.valid) {
        when(io.in.bits.is_load || io.in.bits.isS_type){  //需要与SRAM通信
          valid := true.B
          state := sHandshake
        }.otherwise{
          state := sValid
        }
      }
    }
    is(sHandshake) {
      when(io.isu_axi_out.ready){  //AXI ready，可以与SRAM通信
        when(io.in.bits.is_load) {
          arvalid := true.B
          state   := sLoad
        }.elsewhen(io.in.bits.isS_type) {
          awvalid := true.B
          state   := sStoreAddr
        }
      }
    }
    is(sLoad) {
      when(io.isu_axi_out.bits.arvalid && io.isu_axi_in.bits.arready) {
        arvalid := false.B
        state   := sValid
      }
    }
    is(sStoreAddr) {
      when(io.isu_axi_out.bits.awvalid && io.isu_axi_in.bits.awready) {
        awvalid := false.B
        wvalid  := true.B
        state   := sStoreData
      }
    }
    is(sStoreData) {
      when(io.isu_axi_out.bits.wvalid && io.isu_axi_in.bits.wready) {
        wvalid := false.B
        state  := sValid
      }
    }
    is(sValid) {
      when(io.in.bits.is_load) {
        when(io.isu_axi_in.bits.rvalid && io.isu_axi_in.bits.rresp === 0.U){
          load_finish := true.B
          valid := false.B
          state := sIdle
        }
      }.elsewhen(io.in.bits.isS_type) {
        when(io.isu_axi_in.bits.bresp === 0.U && io.isu_axi_in.bits.bvalid){
          store_finish := true.B
          valid := false.B
          state := sIdle
        }
      }.otherwise{
        state := sIdle
      }
    }
  }

  io.out.bits.dm_out       := dm_out
  io.out.bits.alu_out      := io.in.bits.alu_out
  io.out.bits.jump_jalr    := io.in.bits.jump_jalr
  io.out.bits.jump_en      := io.in.bits.jump_en
  io.out.bits.imm          := io.in.bits.imm
  io.out.bits.is_ecall     := io.in.bits.is_ecall
  io.out.bits.is_mret      := io.in.bits.is_mret
  io.out.bits.mtvec        := io.in.bits.mtvec
  io.out.bits.epc          := io.in.bits.epc
  io.out.bits.rd1          := io.in.bits.rd1
  io.out.bits.mem_rd_en    := io.in.bits.mem_rd_en
  io.out.bits.mem_wr_en    := io.in.bits.mem_wr_en
  io.out.bits.rf_wr_en     := io.in.bits.rf_wr_en
  io.out.bits.load_finish  := load_finish
  io.out.bits.store_finish := store_finish
  io.out.bits.is_csr       := io.in.bits.is_csr
  
  // For WBU
  io.out.bits.is_cmp   := io.in.bits.is_cmp
  io.out.bits.is_load  := io.in.bits.is_load
  io.out.bits.isS_type := io.in.bits.isS_type

  io.out.bits.is_j := io.in.bits.is_j
  io.out.bits.pc   := io.in.bits.pc

  io.isu_axi_out.valid := valid
  io.isu_axi_in.ready  := valid
  io.in.ready          := (state === sIdle)
  io.out.valid         := (state === sValid)
}