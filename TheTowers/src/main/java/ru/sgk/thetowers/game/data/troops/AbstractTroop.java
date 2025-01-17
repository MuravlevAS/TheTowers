package ru.sgk.thetowers.game.data.troops;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import ru.sgk.thetowers.game.data.teams.GameTeam;
import ru.sgk.thetowers.utils.CustomPathfinder;

public abstract class AbstractTroop
{
	/** Секция моба в конфиге*/
	protected ConfigurationSection configSection;
	private String title;
	private double damage;
	private double speed;
	/** Стоимость одного моба при посылании его (на хуй) другой команде*/
	private double cost;
	private double health;
	/** Сколько денег принесёт при убийстве*/
	private double killed;
	/** Сам моб*/
	private LivingEntity entity;
	/** Тип моба*/
	private EntityType mobType;
	/** Локация спавна*/
	private Location spawnLocation;
	/** Локация конца пути*/
	private Location endLocation;
	private List<Location> wayPoints;
	/** Был ли моб убит */
	private boolean isKilled;
	/** Является ли моб невидимым*/
	private boolean invisible = false;
	/** Текущая точка пути*/
	private int currentWayPoint;
	/** Куда моб направляется в данное время*/
    private Location movingTo;
    /** Команда, к которой моб относится*/
    private GameTeam parentTeam;
    /** Был ли моб деспавнен. true, когда моб доходит до конца и наносит урон команде*/
	private boolean despawned;

	public AbstractTroop(GameTeam parentTeam)
    {
        this.parentTeam = parentTeam;
		currentWayPoint = 0;
		despawned = false;
	}
	/**
	 * Спавнит моба в его spawnLocation
	 */
	public void spawn()
	{
		if (entity == null)
		{
			// Спавним моба.
			entity = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, mobType);
			// Устанавливаем ему кастомное имя, что быб он вдруг не деспавнился
            entity.setCustomName(ChatColor.UNDERLINE + "");
            // Устанавливаем ему скорость передвижения и количество здоровья
			entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
			entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
			entity.setHealth(health);
			entity.setRemoveWhenFarAway(false);
			
			entity.setCollidable(false);
			// Деаем моба неуязвимым, что бы игроки не могли его убить своими погаными ручками
			entity.setInvulnerable(true);
			// Надеваем мобу кнопку на голову, что бы не горел, если это зомби или скелет.
			try
			{
				entity.getEquipment().clear();
				entity.getEquipment().setHelmet(new ItemStack(Material.STONE_BUTTON));
			}
			catch (Exception e) {}

			if (entity instanceof Zombie)
			{
				((Zombie) entity).setBaby(false);
			}

			try {
				entity.getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(speed);
			} catch (Exception e) {
			}
		}
//		EntityLiving living = (EntityLiving) entity;
//		living.setSlot(EnumItemSlot.HEAD, new ItemStack(Blocks.STONE_BUTTON));
	}
	/**
	 * Спавнит моба в определённой локации. Перед спавном просто вызывает {@link #setSpawnLocation(Location) setSpawnLocation(loc)); 
	 * @param loc Локация, в которой моб будет заспавнен
	 */
	public void spawn(Location loc) {
		this.setSpawnLocation(loc);
		spawn();
	}
	/**
	 * Деспавнит моба. После выполнения despawned = true 
	 */
	public void despawn(){
		if (entity != null) {
			entity.remove();
			despawned = true;
		}
	}
	/**
	 * Убивает моба. После выполнения isKilled = true;
	 */
	public void kill(){
		if (entity != null){
			isKilled = true;
			entity.damage(entity.getHealth());

		}
	}
	/**
	 * Дамажит моба
	 * @param damage - сколько дамага нанести мобу
	 */
	public void sendDamage(double damage){
		if (entity != null){
			if (damage >= entity.getHealth())
				isKilled = true;
			else isKilled = false;
			entity.damage(damage);
		}
	}

	public void move(Location loc)
	{
        EntityInsentient insentient = ((EntityInsentient)((CraftEntity)entity).getHandle());
//        PathEntity pathEntity = insentient.getNavigation().a(loc.getBlockX(), loc.getBlockX(), loc.getBlockX(),0);
//        insentient.getNavigation().a(pathEntity, 1D);
        insentient.targetSelector.a(PathfinderGoal.Type.MOVE);

        PathfinderGoal pathfinder = new CustomPathfinder(insentient, loc, 1D);

        if (pathfinder.a())
        {
            pathfinder.c();
            pathfinder.e();
        }
        movingTo = loc;
	}

	public void moveNext()
	{

		int x = entity.getLocation().getBlockX();
		int y = entity.getLocation().getBlockY();
		int z = entity.getLocation().getBlockZ();
		// d - destination
		int dx = movingTo.getBlockX();
		int dy = movingTo.getBlockY();
		int dz = movingTo.getBlockZ();
        if (wayPoints.size() >= currentWayPoint)
        {
            if (movingTo == null || (x == dx && y == dy && z == dz))
            {
                move(wayPoints.get(currentWayPoint - 1));
                currentWayPoint++;
            }
            else move(wayPoints.get(currentWayPoint));
        }
        else
        {
            move(endLocation);
        }
        // Если моб достиг конца, заканчиваем его путешествие.
        if (movingTo.equals(endLocation) && (x == dx && y == dy && z == dz))
        {
            endMoving();
        }
	}
	/**
	 * Деспавнит моба и наносит урон команде, которой моб пренадлежит.
	 */
	public void endMoving()
    {
        parentTeam.sendDamage(this);
//        kill();
        despawn();
        // todo: end of moving
    }

    public GameTeam getParentTeam()
    {
        return parentTeam;
    }

    public void setParentTeam(GameTeam parentTeam)
    {
        this.parentTeam = parentTeam;
    }

    public void addWayPoint(Location loc)
    {
        wayPoints.add(loc);
    }

    /**
     * Удаляет ближайшую точку на расстоянии <b>range</b> блоков от локации <b>loc</b>
     */
    public Location removeWayPoint(Location loc, int range)
    {
        if (wayPoints.size() > 0)
        {
            Location point0 = wayPoints.get(0);
            double dx0 = point0.getX() - loc.getX();
            double dy0 = point0.getY() - loc.getY();
            double dz0 = point0.getZ() - loc.getZ();
            double minL = Math.sqrt(dx0 * dx0 + dy0 * dy0 + dz0 * dz0);
            int minI = -1;
            for (int i = 0; i < wayPoints.size(); i++)
            {
                double dx = wayPoints.get(i).getX() - loc.getX();
                double dy = wayPoints.get(i).getY() - loc.getY();
                double dz = wayPoints.get(i).getZ() - loc.getZ();
                double l = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (l <= range && minL >= l)
                {
                    minL = l;
                    minI = i;
                }
            }
            if (minI >= 0)
                return wayPoints.remove(minI);
        }
        return null;
    }

    /**
     * Удаляет все точки на расстоянии <b>range</b> от точки <b>loc</b>
     * @return возвращает список удалённых точке
     */
    public List<Location> removeAllWayPointsInRange(Location loc, int range)
    {
        List<Location> removed = Lists.newArrayList();
        int i = 0;
        while (i < wayPoints.size())
        {
            double dx = wayPoints.get(i).getX() - loc.getX();
            double dy = wayPoints.get(i).getY() - loc.getY();
            double dz = wayPoints.get(i).getZ() - loc.getZ();
            double l = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (l <= range)
            {
                removed.add(wayPoints.remove(i));
                wayPoints.remove(i);
                continue;
            }
            i++;
        }
        return removed;
    }

	public void update(){
		// TODO: update troops
//		int x = entity.getLocation().getBlockX();
//		int y = entity.getLocation().getBlockY();
//		int z = entity.getLocation().getBlockZ();
//		// d - destination
//		int dx = movingTo.getBlockX();
//		int dy = movingTo.getBlockY();
//		int dz = movingTo.getBlockZ();

		moveNext();

	}

	public boolean isDespawned() {
		return despawned;
	}

	public void setDespawned(boolean despawned) {
		this.despawned = despawned;
	}

	public boolean isKilled() {
		return isKilled;
	}
//

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

	public int getCurrentWayPoint() {
		return currentWayPoint;
	}

	public void setCurrentWayPoint(int currentWayPoint) {
		this.currentWayPoint = currentWayPoint;
	}
//	public void setKilled(boolean isKilled) {
//		this.isKilled = isKilled;
//	}


    public Location getMovingTo() {
        return movingTo;
    }

    public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public double getDamage() 
	{
		return damage;
	}
	
	public void setDamage(double damage) 
	{
		this.damage = damage;
	}
	
	public double getSpeed() 
	{
		return speed;
	}
	
	public void setSpeed(double speed) 
	{
		this.speed = speed;
	}
	
	public double getCost() 
	{
		return cost;
	}
	
	public void setCost(double cost) 
	{
		this.cost = cost;
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public void setHealth(double troopHealth)
	{
		this.health = troopHealth;
	}
	
	public double getKilled() 
	{
		return killed;
	}
	
	public void setKilled(double killed) 
	{
		this.killed = killed;
	}

	public EntityType getMobType() 
	{
		return mobType;
	}

	public void setMobType(EntityType mobType) 
	{
		this.mobType = mobType;
	}
}
