package npc

import chisel3._
import chisel3.util._

class controller extends Module{
    val io = IO(new Bundle {
        val inst = Input(UInt(32.W))
        val wr_en = Output(Bool())
        val alu_sel = Output(UInt(8.W))
        val pc_jump = Output(Bool())
        val imm = Output(UInt(32.W))
    })
    val opcode = Wire(UInt(7.W))
    opcode := io.inst(6, 0)

    val isR_type = (opcode === "b0110011".U)
    val isI_type = (opcode === "b0010011".U)
    val isS_type = (opcode === "b0100011".U)
    val isB_type = (opcode === "b1100011".U)
    val isU_type = (opcode === "b0110111".U)
    val isJ_type = (opcode === "b1101111".U)

    val fun3 = Wire(UInt(3.W))
    fun3 := io.inst(14, 12)

//根据指令类型提取imm
    when(isI_type){
        val imm_i = Wire(UInt(12.W))
        imm_i := io.inst(31, 20)
        io.imm := Cat(Fill(20, imm_i(12)), imm_i)
    }

    val is_addi = (fun3 === "b000".U)

    when(isI_type && is_addi){
        io.alu_sel := "b0000_0001".U
        
    }



}
