package safro.oxidized;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import safro.oxidized.config.OxidizedConfig;

/** Shared mod identity, logger, and configuration. */
public final class Oxidized {
    public static final String MOD_ID = "oxidized";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static OxidizedConfig CONFIG = new OxidizedConfig();

    private Oxidized() {}
}
