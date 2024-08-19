/* 
const myCoreItems = require("ui/myCoreItems");

Events.on(EventType.ClientLoadEvent, e => {
	let ui = Vars.ui;
	let hudGroup = ui.hudGroup;
	let hudfrag = ui.hudfrag;
	let settings = Core.settings;
	
	myCoreItems.load();
	
	let myItems = myCoreItems.rebuild();
	let collapser = hudGroup.find("coreinfo").getChildren().get(1).getChildren().get(0);
	let oldItems = collapser.getChildren().get(0);
	
	let change = () => {
		let s = settings.getBool("mycoreitems9527", false);
		let set = s ? myItems : oldItems;
		collapser.setTable(set);
	}
	
	// collapser.setCollapsed(boolp(() => !(hudfrag.shown && settings.getBool("coreitems", true))));
	// if(Vars.mobile) collapser.touchable = Touchable.disabled; // 核心资源显示会需要点击吗（
	
	if(Vars.mobile) ui.settings.graphics.checkPref("coreitems", true);
	ui.settings.graphics.checkPref("mycoreitems9527", false, s => change());
	
	change();
});

Events.on(ResetEvent, e => {
	myCoreItems.resetUsed();
});
 */

const myCoreItems = require("ui/myCoreItems");
		Events.on(EventType.ClientLoadEvent, e => {
			let ui = Vars.ui;
			let hudGroup = ui.hudGroup;
			let hudfrag = ui.hudfrag;
			let settings = Core.settings;
			myCoreItems.load();
			let myItems = myCoreItems.rebuild();
			let collapser = hudGroup.find("coreinfo").getChildren().get(1).getChildren().get(0);
			let oldItems = collapser.getChildren().get(0);

			let change = () => {
				let s = settings.getBool("mycoreitems9527", true);
				let set = s ? myItems : oldItems;
				collapser.setTable(set);
			}
			change();
		});
		Events.on(ResetEvent, e => {
			myCoreItems.resetUsed();
		});