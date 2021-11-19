package net.demonly.stickychallenges.menu;

import com.github.dig.gui.GuiTickable;
import com.github.dig.gui.inventory.ChestInventoryGui;
import com.github.dig.gui.state.ComponentClickState;
import com.github.dig.gui.state.GuiDragState;
import com.github.dig.gui.state.GuiItemChangeState;
import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.events.EventClaimReward;
import net.demonly.stickychallenges.menu.items.*;

import java.util.Objects;

public class MenuChallenge extends ChestInventoryGui implements GuiTickable {

    private final StickyChallengesPlugin plugin;

    public MenuChallenge(StickyChallengesPlugin plugin) {
        super(5, Objects.requireNonNull(plugin.getLang().getString("menu_challenge_title")));
        this.plugin = plugin;
        createBorder();
        this.inventory.setItem(slotOf(0, 4), ItemCloseButton.get(plugin));
    }

    @Override
    public void handleItemChange(GuiItemChangeState state) {
    }

    @Override
    public void handleComponentClick(ComponentClickState state) {
        if (state.getCurrentItem().equals(ItemCloseButton.get(plugin))) {
            state.getPlayer().closeInventory();
        }

        if (plugin.getPreviousChallenge() != null &&
                state.getCurrentItem().equals(ItemPreviousChallengeSuccess.get(plugin))) {
            new EventClaimReward(plugin).claimReward(state.getPlayer());
            state.getPlayer().closeInventory();
        }
    }

    @Override
    public void handleDrag(GuiDragState state) {
    }

    private void createBorder() {

        // To Do:
        // Math to get border of coordinates, as this is ugly.
        this.inventory.setItem(slotOf(0,0), ItemBorder.get());
        this.inventory.setItem(slotOf(0,1), ItemBorder.get());
        this.inventory.setItem(slotOf(0,2), ItemBorder.get());
        this.inventory.setItem(slotOf(0,3), ItemBorder.get());
        this.inventory.setItem(slotOf(1,0), ItemBorder.get());
        this.inventory.setItem(slotOf(2,0), ItemBorder.get());
        this.inventory.setItem(slotOf(3,0), ItemBorder.get());
        this.inventory.setItem(slotOf(4,0), ItemBorder.get());
        this.inventory.setItem(slotOf(5,0), ItemBorder.get());
        this.inventory.setItem(slotOf(6,0), ItemBorder.get());
        this.inventory.setItem(slotOf(7,0), ItemBorder.get());
        this.inventory.setItem(slotOf(8,0), ItemBorder.get());
        this.inventory.setItem(slotOf(8,1), ItemBorder.get());
        this.inventory.setItem(slotOf(8,2), ItemBorder.get());
        this.inventory.setItem(slotOf(8,3), ItemBorder.get());
        this.inventory.setItem(slotOf(8,4), ItemBorder.get());
        this.inventory.setItem(slotOf(1,4), ItemBorder.get());
        this.inventory.setItem(slotOf(2,4), ItemBorder.get());
        this.inventory.setItem(slotOf(3,4), ItemBorder.get());
        this.inventory.setItem(slotOf(4,4), ItemBorder.get());
        this.inventory.setItem(slotOf(5,4), ItemBorder.get());
        this.inventory.setItem(slotOf(6,4), ItemBorder.get());
        this.inventory.setItem(slotOf(7,4), ItemBorder.get());
    }

    @Override
    public void tick(int tickCount) {
        this.inventory.setItem(slotOf(2, 2), ItemCurrentChallenge.get(plugin));
        if (plugin.getPreviousChallenge() != null) {
            if (plugin.getPreviousChallenge().getCurrentAmount() >= plugin.getPreviousChallenge().getAmount()) {
                this.inventory.setItem(slotOf(6, 2), ItemPreviousChallengeSuccess.get(plugin));
            } else {
                this.inventory.setItem(slotOf(6, 2), ItemPreviousChallengeFail.get(plugin));
            }
        } else {
            this.inventory.setItem(slotOf(6,2), ItemPreviousChallengeNone.get(plugin));
        }
    }
}
