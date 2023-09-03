package fi.dy.esav.Minecart_speedplus;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class Minecart_speedplusVehicleListener implements org.bukkit.event.Listener
{
  int[] xmodifier = { -1, 0, 1 };
  int[] ymodifier = { -2, -1, 0, 1, 2 };
  int[] zmodifier = { -1, 0, 1 };
  int cartx;
  int carty;
  int cartz;
  int blockx;
  int blocky;
  int blockz;
  Block block;
  String blockid;
  double line1;
  public static Minecart_speedplus plugin;
  Logger log = Logger.getLogger("Minecraft");
  
  boolean error;
  
  Vector flyingmod = new Vector(10.0D, 0.01D, 10.0D);
  Vector noflyingmod = new Vector(1, 1, 1);
  
  public Minecart_speedplusVehicleListener(Minecart_speedplus instance)
  {
    plugin = instance;
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void onVehicleCreate(VehicleCreateEvent event) {
    if ((event.getVehicle() instanceof Minecart))
    {
      Minecart cart = (Minecart)event.getVehicle();
      cart.setMaxSpeed(0.4D * Minecart_speedplus.getSpeedMultiplier());
    }
  }
  

  @EventHandler(priority=EventPriority.NORMAL)
  public void onVehicleMove(VehicleMoveEvent event)
  {
    if ((event.getVehicle() instanceof Minecart))
    {
      Minecart cart = (Minecart)event.getVehicle();
      for (int xmod : this.xmodifier) {
        for (int ymod : this.ymodifier) {
          for (int zmod : this.zmodifier)
          {
            this.cartx = cart.getLocation().getBlockX();
            this.carty = cart.getLocation().getBlockY();
            this.cartz = cart.getLocation().getBlockZ();
            this.blockx = (this.cartx + xmod);
            this.blocky = (this.carty + ymod);
            this.blockz = (this.cartz + zmod);
            this.block = cart.getWorld().getBlockAt(this.blockx, this.blocky, this.blockz);
            
            this.blockid = cart.getWorld().getBlockAt(this.blockx, this.blocky, this.blockz).getBlockData().getMaterial().toString();
            

            if ((this.blockid == Material.OAK_WALL_SIGN.toString()) || (this.blockid == Material.OAK_SIGN.toString()))
            {
              Sign sign = (Sign)this.block.getState();
              String[] text = sign.getLines();
              if (text[0].equalsIgnoreCase("[msptitle]")) {
                  if(cart.getPassengers().size()==1) {
                	  if(cart.getPassengers().get(0) instanceof Player) {
                		  Player p = (Player)cart.getPassengers().get(0);
            			  plugin.titleApi.sendTitleWithPlaceholders(p,text[1]);
            			  plugin.titleApi.sendSubtitleWithPlaceholders(p,text[2]);
            			  plugin.titleApi.sendActionbarWithPlaceholders(p,text[3]);
                	  }
                  }
              }
              
              if (text[0].equalsIgnoreCase("[mspenter]")) {
                  if(cart.getPassengers().size()==1) {
                	  if(cart.getPassengers().get(0) instanceof Player) {
                		  Player p = (Player)cart.getPassengers().get(0);
            			  plugin.titleApi.sendTitleWithPlaceholders(p, "&e即将进站"+text[1]);
            			  plugin.titleApi.sendSubtitleWithPlaceholders(p,"已进入安全区域");
            			  plugin.titleApi.sendActionbarWithPlaceholders(p,"感谢乘坐天际轨道交通");
                	  }
                  }
              }
              
              if (text[0].equalsIgnoreCase("[mspexit]")) {
                  if(cart.getPassengers().size()==1) {
                	  if(cart.getPassengers().get(0) instanceof Player) {
                		  Player p = (Player)cart.getPassengers().get(0);
            			  plugin.titleApi.sendTitleWithPlaceholders(p, "&e前方到站:"+text[1]);
            			  plugin.titleApi.sendSubtitleWithPlaceholders(p,"即将离开安全区域");
            			  plugin.titleApi.sendActionbarWithPlaceholders(p,"欢迎乘坐轨道交通");
                	  }
                  }
              }
              
              if (text[0].equalsIgnoreCase("[s]")) {
                  if(cart.getPassengers().size()==1) {
                	  if(cart.getPassengers().get(0) instanceof Player) {
                		  Player p = (Player)cart.getPassengers().get(0);
                		  p.playSound(p.getLocation(),text[1],100,1);
                	  }
                  }
              }
              
              
              if (text[0].equalsIgnoreCase("[msp]"))
              {
                if (text[1].equalsIgnoreCase("fly")) {
                  cart.setFlyingVelocityMod(this.flyingmod);
                }
                else if (text[1].equalsIgnoreCase("nofly"))
                {
                  cart.setFlyingVelocityMod(this.noflyingmod);
                }
                else
                {
                  this.error = false;
                  try
                  {
                    this.line1 = Double.parseDouble(text[1]);
                  }
                  catch (Exception e)
                  {
                    sign.setLine(2, "  ERROR");
                    sign.setLine(3, "WRONG VALUE");
                    sign.update();
                    this.error = true;
                  }
                  
                  if (!this.error)
                  {
                    if (((0.0D < this.line1 ? 1 : 0) & (this.line1 <= 50.0D ? 1 : 0)) != 0)
                    {
                      cart.setMaxSpeed(0.4D * Double.parseDouble(text[1]));
                      if(cart.getPassengers().size()==1) {
                    	  if(cart.getPassengers().get(0) instanceof Player) {
                    		  Player p = (Player)cart.getPassengers().get(0);
                    		  //plugin.titleApi.clearTitles(p);
                    		  if(this.line1 < 1.9D) {
                    			  //plugin.titleApi.sendTitleWithPlaceholders(p, " ");
                    			  plugin.titleApi.sendActionbarWithPlaceholders(p,"&e列车正在减速");
                    		  }else {
                    			  //plugin.titleApi.sendTitleWithPlaceholders(p, " ");
                    			  plugin.titleApi.sendActionbarWithPlaceholders(p,"&e列车正在加速,请保持前进方向");
                    		  }
                    	  }
                      }
                    }
                    else
                    {
                      sign.setLine(2, "  ERROR");
                      sign.setLine(3, "WRONG VALUE");
                      sign.update();
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
