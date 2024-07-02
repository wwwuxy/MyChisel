package npc

import chisel3._
import chisel3.util._

class controller extends Module{
    val io = IO(new Bundle {
        val inst = Input(UInt(32.W))
        val rf_wr_en = Output(Bool())
        val rf_wr_sel = Output(Bool())
        val alu_a_sel = Output(Bool())
        val alu_b_sel = Output(Bool())
        val mem_wr = Output(Bool())
        val alu_sel = Output(UInt(12.W))
        val jump_en = Output(Bool())
        val imm = Output(UInt(32.W))
        // val nemutrap = Output(Bool())
    })

//inital enable signal
    io.rf_wr_en := false.B
    io.jump_en := false.B
    io.alu_sel := 0.U
    io.rf_wr_sel := false.B
    io.imm := 0.U
    io.alu_a_sel := false.B
    io.alu_b_sel := false.B
    io.mem_wr := false.B
    io.alu_sel := 0.U
    // io.nemutrap := false.B

//根据opcode确定指令类型
    val opcode = Wire(UInt(7.W))
    opcode := io.inst(6, 0)
    val isR_type = (opcode === "b0110011".U)
    val isI_type = (opcode === "b0010011".U)
    val isS_type = (opcode === "b0100011".U)
    val isB_type = (opcode === "b1100011".U)
    val isJ_type = (opcode === "b1101111".U)

//提取fun3字段，确定指令
    val fun3 = Wire(UInt(3.W))
    fun3 := io.inst(14, 12)

//auipc和lui指令通过opcode进行区分
    val is_auipc = (opcode === "b0010111".U)
    val is_lui = (opcode === "b0110111".U)

//根据指令类型提取imm
    when(isI_type){
        val imm_i = Wire(UInt(12.W))
        imm_i := io.inst(31, 20)
        io.imm := Cat(Fill(20, imm_i(11)), imm_i)
    }
    when(is_auipc){
        val imm_u = Wire(UInt(20.W))
        imm_u := io.inst(31, 12)
        io.imm := (Cat(Fill(12, imm_u(19)), imm_u)) << 12
    }
    when(is_lui){
        val imm_u = Wire(UInt(20.W))
        imm_u := io.inst(31, 12)
        io.imm := (Cat(Fill(12, imm_u(19)), imm_u)) << 12
    }

    val is_addi = (fun3 === "b000".U)

//lui
    when(is_lui){
        io.alu_sel := "b0100_0000".U
        io.alu_a_sel := false.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := true.B
    }
// auipc
    when(is_auipc){
        io.alu_sel := "b0000_0001".U
        io.alu_a_sel := false.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := true.B
    }

// addi
    when(isI_type && is_addi){
        io.alu_sel := "b0000_0001".U
        io.alu_a_sel := true.B
        io.alu_b_sel := false.B
        io.rf_wr_en := true.B
        io.rf_wr_sel := true.B
    }
}
