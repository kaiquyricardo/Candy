package com.github.kaiquy.candy;

import com.github.kaiquy.candy.commands.CandyCommand;
import com.github.kaiquy.candy.commands.DiabetesCommand;
import com.github.kaiquy.candy.commands.InsulinCommand;
import com.github.kaiquy.candy.data.candy.CandyCache;
import com.github.kaiquy.candy.data.candy.CandyController;
import com.github.kaiquy.candy.data.insulin.InsulinCache;
import com.github.kaiquy.candy.data.insulin.InsulinController;
import com.github.kaiquy.candy.data.user.UserCache;
import com.github.kaiquy.candy.data.user.UserController;
import com.github.kaiquy.candy.data.user.UserStorage;
import com.github.kaiquy.candy.database.MySQLProvider;
import com.github.kaiquy.candy.hook.PlaceholderHook;
import com.github.kaiquy.candy.listener.GeneralListener;
import com.github.kaiquy.candy.provider.DefaultRegistry;
import com.github.kaiquy.candy.provider.SpigotProvider;
import com.github.kaiquy.candy.task.DiabetesTask;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public class CandyPlugin extends JavaPlugin {

    private MySQLProvider mySQLProvider;
    private UserCache userCache;
    private UserController userController;
    private UserStorage userStorage;
    private CandyCache candyCache;
    private CandyController candyController;
    private InsulinCache insulinCache;
    private InsulinController insulinController;
    private DiabetesTask diabetesTask;
    private DefaultRegistry defaultRegistry;
    private PlaceholderHook placeholderHook;

    @Override
    public void onLoad() {
        mySQLProvider = new MySQLProvider(
                getConfig().getString("mysql.host"),
                getConfig().getInt("mysql.port"),
                getConfig().getString("mysql.database"),
                getConfig().getString("mysql.username"),
                getConfig().getString("mysql.password")
        );
        userCache = new UserCache();
        userController = new UserController(this);
        userStorage = new UserStorage();
        candyCache = new CandyCache();
        candyController = new CandyController();
        insulinCache = new InsulinCache();
        insulinController = new InsulinController();
        defaultRegistry = new DefaultRegistry(this);
        placeholderHook = new PlaceholderHook();
        diabetesTask = new DiabetesTask();

    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        mySQLProvider.init();

        loadRegistry();
    }

    @Override
    public void onDisable() {
        mySQLProvider.closeConnection();
    }

    public void loadRegistry() {
        candyController.load(getConfig());
        insulinController.load(getConfig());

        defaultRegistry.registerCommands(
                new CandyCommand(),
                new InsulinCommand(),
                new DiabetesCommand()
        );

        defaultRegistry.registerListener(
                new SpigotProvider(),
                new GeneralListener()
        );

        placeholderHook.register();
        diabetesTask.start();
    }

    public static CandyPlugin getInstance() {
        return getPlugin(CandyPlugin.class);
    }
}
