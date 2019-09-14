package com.Soara.Light.LightBinding;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LightBinding
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
  private double stunChance;
  private double stunDuration;
  private boolean controllable;
  
  public LightBinding(Player player)
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
    this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.SoaraCaulfield.Light.LightBinding.Cooldown");
    this.range = ConfigManager.getConfig().getDouble("ExtraAbilities.SoaraCaulfield.Light.LightBinding.Range");
    this.stunChance = ConfigManager.getConfig().getDouble("ExtraAbilities.SoaraCaulfield.Light.LightBinding.StunChance");
    this.stunDuration = ConfigManager.getConfig().getDouble("ExtraAbilities.SoaraCaulfield.Light.LightBinding.StunDuration");
    this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.SoaraCaulfield.Light.LightBinding.Damage");
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
    
	ParticleEffect.END_ROD.display(this.location, 8, 0.2f, 0.2f, 0.2f, 0f);
	ParticleEffect.CLOUD.display(this.location, 3, 0f, 0f, 0f, 0f);
    GeneralMethods.displayColoredParticle("FFBFE8", this.location, 3, 0.5, 0, 0.5);
    GeneralMethods.displayColoredParticle("F6FF7C", this.location, 3, 0.5, 0, 0.5);
    GeneralMethods.displayColoredParticle("A8F1FF", this.location, 3, 0.5, 0, 0.5);
    GeneralMethods.displayColoredParticle("FFC69E", this.location, 3, 0.5, 0, 0.5);
    this.location.getWorld().playSound(this.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 1.0F);
    if (GeneralMethods.isSolid(this.location.getBlock()))
    {
      remove();
      return;
    }
    for (Entity entity : GeneralMethods.getEntitiesAroundPoint(this.location, 3.0)) {
      if (((entity instanceof LivingEntity)) && (entity.getUniqueId() != this.player.getUniqueId()))
      {
        DamageHandler.damageEntity(entity, this.damage, this);
        electrocute(entity);
        return;
      }
    }
  }
	public void electrocute(Entity entity) {

		entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.01F);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.01F);
	    GeneralMethods.displayColoredParticle("FFBFE8", player.getLocation(), 3, 1, 1, 1);
	    GeneralMethods.displayColoredParticle("F6FF7C", player.getLocation(), 3, 1, 1, 1);
	    GeneralMethods.displayColoredParticle("A8F1FF", player.getLocation(), 3, 1, 1, 1);
	    GeneralMethods.displayColoredParticle("FFC69E", player.getLocation(), 3, 1, 1, 1);
	    GeneralMethods.displayColoredParticle("FFBFE8", entity.getLocation(), 3, 1, 1, 1);
	    GeneralMethods.displayColoredParticle("F6FF7C", entity.getLocation(), 3, 1, 1, 1);
	    GeneralMethods.displayColoredParticle("A8F1FF", entity.getLocation(), 3, 1, 1, 1);
	    GeneralMethods.displayColoredParticle("FFC69E", entity.getLocation(), 3, 1, 1, 1);

		DamageHandler.damageEntity(entity, damage, this);
		
		if (Math.random() < stunChance) {
			final Location lentLoc = entity.getLocation();
			final Entity e = entity;

			new BukkitRunnable() {
				int count = 0;
				@Override
				public void run() {
					if (e.isDead() || (e instanceof Player && !((Player) e).isOnline())) {
						cancel();
						return;
					}

					Location tempLoc = lentLoc.clone();
					Vector tempVel = e.getVelocity();
					tempVel.setY(Math.min(0, tempVel.getY()));
					tempLoc.setY(e.getLocation().getY());
					e.teleport(tempLoc);
					e.setVelocity(tempVel);
					count++;
					if (count > stunDuration) {
						cancel();
					}
				}
			}.runTaskTimer(ProjectKorra.plugin, 0, 1);
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
    return "LightBinding";
  }
  
  public String getDescription()
  {
    return "|Add-On by Soara|\n Shoot an orb of Light that stuns!";
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
    ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new LBListener(), ProjectKorra.plugin);
    ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());
    
    this.perm = new Permission("bending.ability.LightBinding");
    ProjectKorra.plugin.getServer().getPluginManager().addPermission(this.perm);
    this.perm.setDefault(PermissionDefault.TRUE);
    
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.LightBinding.Cooldown", Integer.valueOf(4000));
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.LightBinding.Range", Integer.valueOf(20));
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.LightBinding.StunDuration", Integer.valueOf(2));
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.LightBinding.StunChance", Integer.valueOf(2));
    ConfigManager.getConfig().addDefault("ExtraAbilities.SoaraCaulfield.Light.LightBinding.Damage", Integer.valueOf(2));
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
