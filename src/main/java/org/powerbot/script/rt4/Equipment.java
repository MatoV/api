package org.powerbot.script.rt4;

import org.powbot.stream.item.EquipmentItemStream;
import org.powbot.stream.item.ItemStream;
import org.powbot.stream.Streamable;

import java.util.ArrayList;
import java.util.List;

/**
 * Equipment
 * A utility class for interacting with worn items on the player.
 */
public class Equipment extends ItemQuery<Item> implements Streamable<EquipmentItemStream> {
	public Equipment(final ClientContext factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> get() {
		final List<Item> items = new ArrayList<>(11);
		final int[] data = ctx.players.local().appearance();
		for (final Slot slot : Slot.values()) {
			final int index = slot.getIndex();
			final Component c = ctx.widgets.widget(Constants.EQUIPMENT_WIDGET).component(slot.getComponentIndex()).component(1);
			final boolean v = c.visible();
			if (index < 0 || (index >= data.length || data[index] < 0) && !v) {
				continue;
			}
			items.add(new Item(ctx, c, v ? c.itemId() : data[index], v ? c.itemStackSize() : 1));
		}
		return items;
	}

	/**
	 * Returns the {@link org.powerbot.script.rt4.Item} at the spcified {@link Slot}.
	 *
	 * @param slot the {@link Slot} to get the {@link org.powerbot.script.rt4.Item} at
	 * @return the {@link org.powerbot.script.rt4.Item} in the provided slot
	 */
	public Item itemAt(final Slot slot) {
		final int index = slot.getIndex();
		final int[] data = ctx.players.local().appearance();
		if (index < 0) {
			return nil();
		}
		final Component c = ctx.widgets.widget(Constants.EQUIPMENT_WIDGET).component(slot.getComponentIndex()).component(1);
		final boolean v = c.visible();
		if ((index >= data.length || data[index] < 0) && !v) {
			return nil();
		}
		return new Item(ctx, c, v ? c.itemId() : data[index], v ? c.itemStackSize() : 1);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item nil() {
		return Item.NIL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EquipmentItemStream toStream() {
		return new EquipmentItemStream(ctx, get().stream());
	}

	/**
	 * An enumeration of equipment slots.
	 */
	public enum Slot {
		HEAD(0, 15),
		CAPE(1, 16),
		NECK(2, 17),
		MAIN_HAND(3, 18),
		TORSO(4, 19),
		OFF_HAND(5, 20),
		LEGS(7, 21),
		HANDS(9, 22),
		FEET(10, 23),
		RING(12, 24),
		QUIVER(13, 25);
		private final int index, component;

		Slot(final int index, final int component) {
			this.index = index;
			this.component = component;
		}

		public int getIndex() {
			return index;
		}

		public int getComponentIndex() {
			return component;
		}
	}
}
