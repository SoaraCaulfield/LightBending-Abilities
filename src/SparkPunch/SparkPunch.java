package com.Soara.Light.SparkPunch;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.Soara.LightBending.LightAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

public class SparkPunch
  extends LightAbility
  implements AddonAbility
{
  private Permission perm;
  private Location location;
  private Location origin;
  private Vector direction;
  private long cooldown;
  private double range;
  private double damage;
  private boolean controllable;
  
  public SparkPunch(Player player)
  {
    super(player);
    if (this.bPlayer.isOnCooldown(this)) {
      return;
    }
    setFields();
    start();
    this.bPlayer.addCooldown(this);
  }
  
  private void setFields()
  {
    this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Cooldown");
    this.range = ConfigManager.getConfig().getDouble("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Range");
    this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Damage");
    this.controllable = ConfigManager.getConfig().getBoolean("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Controllable");
    this.origin = this.player.getLocation().clone().add(0.0D, 1.0D, 0.0D);
    this.location = this.origin.clone();
    this.direction = this.player.getLocation().getDirection();
  }
  
  public void progress()
  {
    if ((this.player.isDead()) || (!this.player.isOnline()))
    {
      remove();
      return;
    }
    if (this.origin.distance(this.location) > this.range)
    {
      remove();
      return;
    }
    if (this.controllable) {
      this.direction = this.player.getLocation().getDirection();
    }
    this.location.add(this.direction.multiply(1));
    
	ParticleEffect.END_ROD.display(this.location, 1, 0.2f, 0.2f, 0.2f, 0f);
    GeneralMethods.displayColoredParticle("FFBFE8", this.location, 3, 0.1, 0.1, 0.1);
    GeneralMethods.displayColoredParticle("F6FF7C", this.location, 3, 0.1, 0.1, 0.1);
    GeneralMethods.displayColoredParticle("FFAB51", this.location, 6, 0.3, 0.3, 0.3);
    GeneralMethods.displayColoredParticle("FFAB51", this.location, 2, 0, 0, 0);
    GeneralMethods.displayColoredParticle("FFAB51", this.location, 6, 0.1, 0.1, 0.1);
    GeneralMethods.displayColoredParticle("FFC69E", this.location, 3, 0.1, 0.1, 0.1);
    this.location.getWorld().playSound(this.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 2.0F);
    if (GeneralMethods.isSolid(this.location.getBlock()))
    {
      remove();
      return;
    }
    for (Entity entity : GeneralMethods.getEntitiesAroundPoint(this.location, 3.0)) {
      if (((entity instanceof LivingEntity)) && (entity.getUniqueId() != this.player.getUniqueId()))
      {
        DamageHandler.damageEntity(entity, this.damage, this);
        return;
      }
    }
  }
  public long getCooldown()
  {
    return this.cooldown;
  }
  
  public Location getLocation()
  {
    return null;
  }
  
  public String getName()
  {
    return "SparkPunch";
  }
  
  public String getDescription()
  {
    return "|Add-On by Soara|\n Punch your enemies with sparks!";
  }
  
  public String getInstructions()
  {
    return "Use leftclick!";
  }
  
  public String getAuthor()
  {
    return "SoaraCaulfield";
  }
  
  public String getVersion()
  {
    return "r1.0";
  }
  
  public boolean isHarmlessAbility()
  {
    return false;
  }
  
  public boolean isSneakAbility()
  {
    return false;
  }
  
  public void load()
  {
    ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new SPListener(), ProjectKorra.plugin);
    ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());
    
    this.perm = new Permission("bending.ability.SparkPunch");
    ProjectKorra.plugin.getServer().getPluginManager().addPermission(this.perm);
    this.perm.setDefault(PermissionDefault.TRUE);
    
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Cooldown", Integer.valueOf(800));
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Range", Integer.valueOf(20));
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Damage", Integer.valueOf(2));
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.SparkPunch.Controllable", Boolean.valueOf(true));
    ConfigManager.defaultConfig.save();
  }
  
  public void stop()
  {
    ProjectKorra.log.info("Successfully disabled " + getName() + " by " + getAuthor());
    super.remove();
  }

  	@Override
 	public boolean isExplosiveAbility() {
	return false;
  	}

  	@Override
  	public boolean isIgniteAbility() {
	return false;
  	}
}
