package npc

import chisel3._
import chisel3.util._

class Date_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
//AR
    val arvalid = Input(Bool())
    val araddr = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val arready = Output(Bool())
//R
    val rdata = Output(UInt(32.W))
    val rresp = Output(Bool())
    val rvalid = Output(Bool())
    val rready = Input(Bool())
//AW    
    val awvalid = Input(Bool())
    val awaddr = Input(UInt(32.W))
    val awready = Output(Bool())
//W
    val wvalid = Input(Bool())
    val wdata = Input(UInt(32.W))
    val len = Input(UInt(32.W))
    val wready = Output(Bool())
//B
    val bresp = Output(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
    
    
  })
  addResource("/Date_Memory.v")
}

class ISU extends Module{
    val io = IO(new Bundle{
        val in = Flipped(Decoupled(new EXU_ISU))
        val out = Decoupled(new ISU_WBU)
        val wbu_valid = Input(Bool())
    })

    val Dmem = Module(new Date_Memory())

    Dmem.io.clk := clock
    
    val arvalid = RegInit(false.B) 
    val load_finish = RegInit(false.B)
    val rready = true.B
    val awvalid = RegInit(false.B)
    val store_finish = RegInit(false.B)
    val wvalid = RegInit(false.B)
    val bready = true.B

    when(io.in.bits.is_load){
        when(io.in.valid){
            arvalid := true.B
        }
    }
    
    when(arvalid && Dmem.io.arready){
        arvalid := false.B
    }

    when(Dmem.io.rvalid && rready){
        load_finish := true.B
    }.elsewhen(load_finish){
        load_finish := false.B
    }

    when(io.in.bits.mem_wr_en){
        when(io.in.valid){
            awvalid := true.B
        }
    }

    when(awvalid && Dmem.io.awready){   //写地址握手成功
        awvalid := false.B
        wvalid := true.B
    }

    when(wvalid && Dmem.io.wready){     //写数据握手成功
        wvalid := false.B
    }

    when(Dmem.io.bresp){
        store_finish := true.B
    }.elsewhen(store_finish){
        store_finish := false.B
    }

//Dmem
    Dmem.io.len := io.in.bits.len
    Dmem.io.load_unsign := io.in.bits.load_unsign
    Dmem.io.araddr := io.in.bits.alu_out
    Dmem.io.awaddr := io.in.bits.alu_out
    Dmem.io.wdata := io.in.bits.data
    
    Dmem.io.awvalid := awvalid
    Dmem.io.arvalid := arvalid
    Dmem.io.wvalid := wvalid
    Dmem.io.rready := rready
    Dmem.io.bready := bready

//for wbu
    io.out.bits.dm_out := Dmem.io.rdata
    io.out.bits.alu_out := io.in.bits.alu_out
    io.out.bits.jump_jalr := io.in.bits.jump_jalr
    io.out.bits.jump_en := io.in.bits.jump_en
    io.out.bits.imm := io.in.bits.imm
    io.out.bits.is_ecall := io.in.bits.is_ecall
    io.out.bits.is_mret := io.in.bits.is_mret
    io.out.bits.mtvec := io.in.bits.mtvec
    io.out.bits.epc := io.in.bits.epc
    io.out.bits.rd1 := io.in.bits.rd1
    io.out.bits.mem_rd_en := io.in.bits.mem_rd_en
    io.out.bits.mem_wr_en := io.in.bits.mem_wr_en
    io.out.bits.rf_wr_en := io.in.bits.rf_wr_en
    io.out.bits.load_finish := load_finish
    io.out.bits.store_finish := store_finish
    io.out.bits.is_csr := io.in.bits.is_csr
    
//for wbu
    io.out.bits.is_cmp := io.in.bits.is_cmp
    io.out.bits.is_load := io.in.bits.is_load
    io.out.bits.isS_type := io.in.bits.isS_type

    io.out.bits.is_j := io.in.bits.is_j
    io.out.bits.pc := io.in.bits.pc

  // State Machine
        val sIdle :: sValid :: Nil = Enum(2)
        val state = RegInit(sIdle)    
        switch(state) {
          is(sIdle) {
            when(io.in.valid) {
              state := sValid
            }
          }
          is(sValid) {
              state := sIdle
            }
          }   
        io.in.ready := (state === sIdle)
        io.out.valid := (state === sValid)
}