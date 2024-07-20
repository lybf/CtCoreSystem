
//Java核心系统
function CreatorsPackage(name) {
	var p = Packages.rhino.NativeJavaPackage(name, Vars.mods.mainLoader());
	Packages.rhino.ScriptRuntime.setObjectProtoAndParent(p, Vars.mods.scripts.scope)
	return p
}
var CreatorsJavaPack = CreatorsPackage('CtCoreSystem')
importPackage(CreatorsJavaPack)
importPackage(CreatorsJavaPack.CoreSystem)
importPackage(CreatorsJavaPack.CoreSystem.dialogs)
importPackage(CreatorsJavaPack.CoreSystem.type)
importPackage(CreatorsJavaPack.content)
importPackage(CreatorsJavaPack.ui)
importPackage(CreatorsJavaPack.CoreSystem.type.VXV)
importPackage(CreatorsJavaPack.ui.dialogs)
importPackage(CreatorsJavaPack.ui.dialogs)
require('biansu');
require('CoreItems');
require('nihility');//虚无护盾 

CreatorsModJS.RunName.add("ctcoresystem")
CreatorsModJS.DawnRun.add(run(() => {}));

let mod = Vars.mods.getMod("蓝图效率");
if (mod == null) {
	let bd = Vars.mods.locateMod("ctcoresystem");
	let fi = bd.root.child("mod")
		.child("显示蓝图消耗产出效率[v1.2].zip");
	Vars.mods.importMod(fi);
	Vars.mods.locateMod("蓝图效率");
};

