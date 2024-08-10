// package npc

// import firrtl._
// import firrtl.ir._
// import firrtl.options.Dependency
// import firrtl.passes.Pass
// import firrtl.stage.Forms
// import firrtl.stage.TransformManager.TransformDependency

// class AddPrefixTransform extends Transform with DependencyAPIMigration {

//   // 自定义前缀
//   val customPrefix = "ysyx_22040000_"

//   override def prerequisites: Seq[TransformDependency] = Forms.LowForm
//   override def optionalPrerequisiteOf: Seq[TransformDependency] = Forms.LowEmitters
//   override def invalidates(a: Transform): Boolean = false

//   // 实现转换逻辑
//   def transformModules(modules: Seq[DefModule]): Seq[DefModule] = {
//     modules.map {
//       case m: Module =>
//         m.copy(name = customPrefix + m.name).map(onStmt)
//       case m: ExtModule =>
//         m.copy(name = customPrefix + m.name)
//     }
//   }

//   def onStmt(s: Statement): Statement = s.map(onStmt) match {
//     case d: DefInstance => d.copy(module = customPrefix + d.module)
//     case other => other
//   }

//   override protected def execute(state: CircuitState): CircuitState = {
//     val c = state.circuit
//     val newModules = transformModules(c.modules)
//     val newCircuit = c.copy(modules = newModules, main = customPrefix + c.main)
//     state.copy(circuit = newCircuit)
//   }
// }
