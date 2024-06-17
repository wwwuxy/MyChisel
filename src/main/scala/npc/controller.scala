package npc

import chisel3._
import chisel3.util._

class controller extends Module{
    val io = IO(new Bundle {
        val inst = Input(UInt(32.W))
        val rf_wr_en = Output(Bool())
        val rf_wr_sel = Output(UInt(2.W))
        val alu_a_sel = Output(UInt(2.W))
        val alu_b_sel = Output(UInt(2.W))
        val alu_sel = Output(UInt(12.W))
        val jump_en = Output(Bool())
        val imm = Output(UInt(32.W))
    })
//inital enable signal
    io.rf_wr_en := false.B
    io.jump_en := false.B
    io.alu_sel := 0.U
    io.rf_wr_sel := 0.U
    io.imm := 0.U
    io.alu_a_sel := 0.U
    io.alu_b_sel := 0.U
    io.alu_sel := 0.U

//根据opcode确定指令类型
    val opcode = Wire(UInt(7.W))
    opcode := io.inst(6, 0)
    val isR_type = (opcode === "b0110011".U)
    val isI_type = (opcode === "b0010011".U)
    val isS_type = (opcode === "b0100011".U)
    val isB_type = (opcode === "b1100011".U)
    val isU_type = (opcode === "b0110111".U)
    val isJ_type = (opcode === "b1101111".U)

//提取fun3字段，确定指令
    val fun3 = Wire(UInt(3.W))
    fun3 := io.inst(14, 12)

//根据指令类型提取imm
    when(isI_type){
        val imm_i = Wire(UInt(12.W))
        imm_i := io.inst(31, 20)
        io.imm := Cat(Fill(20, imm_i(11)), imm_i)
    }

    val is_addi = (fun3 === "b000".U)

// addi
    when(isI_type && is_addi){
        io.alu_sel := "b0000_0001".U
        
    }
}

// object controller extends App{
//     emitVerilog(new controller(), Array("--target-dir", "generated"))
// }
