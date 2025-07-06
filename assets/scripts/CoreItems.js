
const myCoreItems = require("ui/myCoreItems");
if (Vars.mobile) {
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
	
	
		collapser.setCollapsed(boolp(() => !(hudfrag.shown && settings.getBool("coreitems", true))));
		 collapser.touchable = Touchable.disabled;
	
	
		 if (Vars.mobile) ui.settings.graphics.checkPref("coreitems", true);//原版的核心显示默认开启或关闭
		// ui.settings.graphics.checkPref("mycoreitems9527", true, s => change());//默认开启或关闭
	
		change();
	});
	
	Events.on(ResetEvent, e => {
		myCoreItems.resetUsed();
	});
} else {
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

}