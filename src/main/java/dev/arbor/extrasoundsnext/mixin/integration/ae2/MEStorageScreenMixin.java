package dev.arbor.extrasoundsnext.mixin.integration.ae2;

import appeng.api.stacks.AEItemKey;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ISortSource;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;

/**
 * AE2 ME 存储终端 mixin，为所有物品交互添加具体物品的音效
 * <p>
 * 根据 AE2 源码分析，handleGridInventoryEntryMouseClick 方法处理以下所有操作：
 * <p>
 * **拾取相关**：
 * - PICKUP_OR_SET_DOWN - 拾取/放下物品到网络 → 播放物品的拾取声音
 * - SPLIT_OR_PLACE_SINGLE - 分裂/放置单个 → 播放物品的拾取声音
 * - PICKUP_SINGLE - 单个拾取 → 播放物品的拾取声音
 * <p>
 * **快速操作**：
 * - SHIFT_CLICK - Shift 点击（快速移动）→ 播放物品的拾取声音
 * - MOVE_REGION - 移动整个区域 → 播放物品的拾取声音
 * <p>
 * **合成相关**：
 * - AUTO_CRAFT - 自动合成 → 播放物品的拾取声音
 * - CRAFT_STACK, CRAFT_ITEM - 合成相关 → 播放物品的拾取声音
 * <p>
 * **容器操作**：
 * - FILL_ITEM - 填充容器 → 播放物品的拾取声音
 * - EMPTY_ITEM - 清空容器 → 播放物品的拾取声音
 * <p>
 * **其他操作**：
 * - CLONE - 创造模式复制 → 播放物品的拾取声音
 * - ROLL_UP, ROLL_DOWN - 滚动调整数量 → 播放物品的拾取声音
 */
@MixinEnvironment
@Mixin(value = MEStorageScreen.class, remap = false)
public abstract class MEStorageScreenMixin<C extends MEStorageMenu> extends AEBaseScreen<C> implements ISortSource {
	public MEStorageScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
		super(menu, playerInventory, title, style);
	}

	/**
	 * 在 handleGridInventoryEntryMouseClick 方法开始处注入声音
	 * <p>
	 * 这个方法专门处理 RepoSlot 的点击，可以访问 GridInventoryEntry
	 * 我们从 entry 中获取物品信息，并播放该物品特有的拾取声音
	 */
	@Inject(method = "handleGridInventoryEntryMouseClick", at = @At("HEAD"), remap = false)
	private void onHandleGridInventoryEntryMouseClick(GridInventoryEntry entry, int mouseButton, ClickType clickType, CallbackInfo ci) {
		if (entry == null) {
			if (!menu.getCarried().isEmpty()) {
				SoundManager.playSound(menu.getCarried(), SoundType.PICKUP);
			}
			return;
		}

		var entryWhat = entry.getWhat();

		if (entryWhat instanceof AEItemKey aeItemKey) {
			//? if >=1.21.1 {
			var itemStack = aeItemKey.getReadOnlyStack();
			//?} else {
			/*var itemStack = aeItemKey.toStack();
			*///?}
			if (!itemStack.isEmpty()) {
				SoundManager.playSound(itemStack, SoundType.PICKUP);
			} else if (!menu.getCarried().isEmpty()) {
				SoundManager.playSound(menu.getCarried(), SoundType.PICKUP);
			}
		}
	}

	/**
	 * 在 setSearchText 改变时添加打字声音
	 */
	@Inject(method = "setSearchText", at = @At("HEAD"), remap = false)
	private void onSearchTextChanged(String text, CallbackInfo ci) {
		if (!text.isEmpty()) {
			SoundManager.keyboard(SoundManager.KeyType.INSERT);
		}
	}

	/**
	 * 在滚动事件中添加滚动声音
	 * <p>
	 * 这个方法处理 Shift+滚轮调整物品数量
	 */
	@Inject(method = "mouseScrolled", at = @At("HEAD"), remap = true)
	//? if >=1.21.1 {
	private void onMouseScrolled(double x, double y, double deltaX, double wheelDelta, CallbackInfoReturnable<Boolean> cir) {
	//?} else {
	/*private void onMouseScrolled(double x, double y, double wheelDelta, CallbackInfoReturnable<Boolean> cir) {
	*///?}
		if (wheelDelta != 0) {
			boolean hasShiftDown = hasShiftDown();
			if (hasShiftDown) {
				SoundManager.keyboard(SoundManager.KeyType.CURSOR);
			}
		}
	}
}
