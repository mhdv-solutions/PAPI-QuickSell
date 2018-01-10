package org.mhdvsolutions.papi.quicksell;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mrCookieSlime.QuickSell.boosters.Booster;

public final class QuickSellExpansion extends PlaceholderExpansion implements Configurable {

    private static final String DEFAULT_CONFIG = "No active boosters.";
    private final Map<String, Function<Player, String>> placeholders;

    public QuickSellExpansion() {
        this.placeholders = new HashMap<>();
        placeholders.put("booster_name", player -> {
            List<Booster> boosters = getActiveBoosters(player);
            if (boosters.isEmpty()) {
                return getString("none-active", DEFAULT_CONFIG);
            }
            return boosters.get(0).getUniqueName();
        });
        placeholders.put("booster_owner", player -> {
            List<Booster> boosters = getActiveBoosters(player);
            if (boosters.isEmpty()) {
                return getString("none-active", DEFAULT_CONFIG);
            }
            return boosters.get(0).getOwner();
        });
        placeholders.put("booster_multiplier", player -> {
            List<Booster> boosters = getActiveBoosters(player);
            if (boosters.isEmpty()) {
                return getString("none-active", DEFAULT_CONFIG);
            }
            return boosters.get(0).getMultiplier().toString();
        });
    }

    @Override
    public String getIdentifier() {
        return "quicksell";
    }

    @Override
    public String getPlugin() {
        return "QuickSell";
    }

    @Override
    public String getAuthor() {
        return "tmux";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().isPluginEnabled(getPlugin());
    }

    @Override
    public Map<String, Object> getDefaults() {
        return new HashMap<String, Object>() {{
            put("none-active", DEFAULT_CONFIG);
        }};
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        if (player == null) {
            return "";
        }

        return placeholders.getOrDefault(placeholder, player1 -> "").apply(player);
    }

    private List<Booster> getActiveBoosters(Player player) {
        return Booster.getBoosters(player.getName()).stream().sorted(Comparator.comparingDouble(Booster::getMultiplier)).collect(Collectors.toList());
    }

}
