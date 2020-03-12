package io.github.vampirestudios.vks.entity;

import io.github.vampirestudios.vks.common.CustomDataParameters;
import io.github.vampirestudios.vks.common.Seat;
import io.github.vampirestudios.vks.common.SeatTracker;
import io.github.vampirestudios.vks.common.entity.PartPosition;
import io.github.vampirestudios.vks.init.ModSounds;
import io.github.vampirestudios.vks.item.SprayCanItem;
import io.github.vampirestudios.vks.utils.Constants;
import io.github.vampirestudios.vks.utils.S2CEntitySpawnPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class VehicleEntity extends Entity  {

    public static final int[] DYE_TO_COLOR = new int[] {16383998, 16351261, 13061821, 3847130, 16701501, 8439583, 15961002, 4673362, 10329495, 1481884, 8991416, 3949738, 8606770, 6192150, 11546150, 1908001};

    protected static final TrackedData<Integer> COLOR = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_SINCE_HIT = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> MAX_HEALTH = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEALTH = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> TRAILER = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected UUID trailerId;
    protected TrailerEntity trailer = null;
    private int searchDelay = 20;

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw;
    protected double lerpPitch;

    protected SeatTracker seatTracker;

    public VehicleEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.seatTracker = new SeatTracker(this);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(TIME_SINCE_HIT, 0);
        this.dataTracker.startTracking(MAX_HEALTH, 100F);
        this.dataTracker.startTracking(HEALTH, 100F);
        this.dataTracker.startTracking(COLOR, 16383998);
        this.dataTracker.startTracking(TRAILER, -1);

        if (world.isClient) {
            onClientInit();
        }

    }

    public void onClientInit() {}

    /* Overridden to prevent odd step sound when driving vehicles. Ain't no subclasses getting
     * the ability to override this. */
    @Override
    protected final void playStepSound(BlockPos pos, BlockState blockIn) {}

    @Override //TODO hmmmmmmmm
    public Box getVisibilityBoundingBox() {
        return this.getBoundingBox().expand(1);
    }

    @Override
    public boolean interact(PlayerEntity player, Hand hand) {
        if(!world.isClient && !player.isSneaking())
        {
            int trailerId = player.getDataTracker().get(CustomDataParameters.TRAILER);
            if(trailerId != -1)
            {
                if(this.getVehicle() == null && this.canTowTrailer() && this.getTrailer() == null)
                {
                    Entity entity = world.getEntityById(trailerId);
                    if(entity instanceof TrailerEntity && entity != this)
                    {
                        TrailerEntity trailer = (TrailerEntity) entity;
                        this.setTrailer(trailer);
                        player.getDataTracker().set(CustomDataParameters.TRAILER, -1);
                    }
                }
                return true;
            }

            ItemStack heldItem = player.getStackInHand(hand);
            if(heldItem.getItem() instanceof SprayCanItem)
            {
                if(this.canBeColored())
                {
                    CompoundTag compound = heldItem.getTag();
                    if(compound != null)
                    {
                        int remainingSprays = compound.getInt("RemainingSprays");
                        if(compound.contains("Color", Constants.NBT.TAG_INT) && remainingSprays > 0)
                        {
                            int color = compound.getInt("Color");
                            if(this.getColor() != color)
                            {
                                this.setColor(compound.getInt("Color"));
                                player.world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.SPRAY_CAN_SPRAY, SoundCategory.PLAYERS, 1.0F, 1.0F);
                                compound.putInt("RemainingSprays", remainingSprays - 1);
                            }
                        }
                    }
                }
                return true;
            }
            /*else if(heldItem.getItem() == ModItems.HAMMER && this.getVehicle() instanceof EntityJack)
            {
                if(this.getHealth() < this.getMaxHealth())
                {
                    heldItem.damage(1, player, playerEntity -> player.sendToolBreakStatus(hand));
                    this.setHealth(this.getHealth() + 5F);
                    this.world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.VEHICLE_THUD, SoundCategory.PLAYERS, 1.0F, 0.8F + 0.4F * random.nextFloat());
                    player.swingHand(hand);
                    if(player instanceof ServerPlayerEntity)
                    {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityAnimationS2CPacket(player, hand == Hand.MAIN_HAND ? 0 : 3));
                    }
                    if(this.getHealth() == this.getMaxHealth())
                    {
                        if(world instanceof ServerWorld)
                        {
                            //TODO send as single packet instead of multiple
                            int count = (int) (50 * (this.getWidth() * this.getHeight()));
                            for(int i = 0; i < count; i++)
                            {
                                double width = this.getWidth() * 2;
                                double height = this.getHeight() * 1.5;

                                Vec3d heldOffset = this.getProperties().getHeldOffset().rotateY((float) Math.toRadians(-this.yaw));
                                double x = this.getX() + width * random.nextFloat() - width / 2 + heldOffset.z * 0.0625;
                                double y = this.getY() + height * random.nextFloat();
                                double z = this.getZ() + width * random.nextFloat() - width / 2 + heldOffset.x * 0.0625;

                                double d0 = random.nextGaussian() * 0.02D;
                                double d1 = random.nextGaussian() * 0.02D;
                                double d2 = random.nextGaussian() * 0.02D;
                                ((ServerWorld) this.world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, d0, d1, d2, 1.0);
                            }
                        }
                        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 1.5F);
                    }
                }
                return true;
            }*/
            else if(this.canAddPassenger(player))
            {
                int seatIndex = this.seatTracker.getClosestAvailableSeatToPlayer(player);
                if(seatIndex != -1)
                {
                    if(player.startRiding(this))
                    {
                        this.getSeatTracker().setSeatIndex(seatIndex, player.getUuid());
                    }
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        if(compound.contains("Color", Constants.NBT.TAG_INT_ARRAY)) {
            int[] c = compound.getIntArray("Color");
            if(c.length == 3) {
                int color = ((c[0] & 0xFF) << 16) | ((c[1] & 0xFF) << 8) | ((c[2] & 0xFF));
                this.setColor(color);
            }
        }
        else if(compound.contains("Color", Constants.NBT.TAG_INT)) {
            int index = compound.getInt("Color");
            if(index >= 0 && index < DYE_TO_COLOR.length) {
                this.setColor(DYE_TO_COLOR[index]);
            }
            compound.remove("Color");
        }
        if(compound.contains("MaxHealth", Constants.NBT.TAG_FLOAT)) {
            this.setMaxHealth(compound.getFloat("MaxHealth"));
        }
        if(compound.contains("Health", Constants.NBT.TAG_FLOAT)) {
            this.setHealth(compound.getFloat("Health"));
        }
        if(compound.containsUuid("Trailer")) {
            this.trailerId = compound.getUuid("Trailer");
        }
        if(compound.contains("SeatTracker", Constants.NBT.TAG_COMPOUND)) {
            this.seatTracker.read(compound.getCompound("SeatTracker"));
        }
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag compound) {
        compound.putIntArray("Color", this.getColorRGB());
        compound.putFloat("MaxHealth", this.getMaxHealth());
        compound.putFloat("Health", this.getHealth());

        //TODO make it save the entity
        if(this.trailerId != null) {
            compound.putUuid("Trailer", this.trailerId);
        }

        compound.put("SeatTracker", this.seatTracker.write());
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> key) {
        super.onTrackedDataSet(key);
        if(world.isClient) {
            if(TRAILER.equals(key)) {
                int entityId = this.dataTracker.get(TRAILER);
                if(entityId != -1) {
                    Entity entity = this.world.getEntityById(this.dataTracker.get(TRAILER));
                    if(entity instanceof TrailerEntity) {
                        this.trailer = (TrailerEntity) entity;
                        this.trailerId = trailer.getUuid();
                    } else {
                        this.trailer = null;
                        this.trailerId = null;
                    }
                } else {
                    this.trailer = null;
                    this.trailerId = null;
                }
            }
        }
    }

    @Override
    public void tick() {
        if(this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        /*this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();*/

        if(!this.world.isClient) {
            if(this.searchDelay <= 0) {
                if(this.trailer != null) {
                    /* Updates periodically to ensure the client knows the vehicle/trailer connection.
                     * There is often problems on loading worlds that it doesn't sync correctly, so this
                     * is the fix. */
                    this.dataTracker.set(TRAILER, trailer.getEntityId());
                    this.trailer.getDataTracker().set(TrailerEntity.PULLING_ENTITY, this.getEntityId());
                    this.searchDelay = /*Config.SERVER.trailerSyncCooldown.get()*/100;
                } else {
                    this.findTrailer();
                }
            } else {
                this.searchDelay--;
            }
        }

        if(!this.world.isClient && this.trailer != null && (!this.trailer.isAlive() || this.trailer.getPullingEntity() != this)) {
            this.setTrailer(null);
        }

        super.tick();
//        this.tickLerp();
        this.onUpdateVehicle();
    }

    private void findTrailer() {
        if(!this.world.isClient && this.trailerId != null && this.trailer == null) {
            ServerWorld server = (ServerWorld) this.world;
            Entity entity = server.getEntity(this.trailerId);
            if(entity instanceof TrailerEntity) {
                this.setTrailer((TrailerEntity) entity);
                return;
            }
            this.trailerId = null;
        }
    }

    protected abstract void onUpdateVehicle();

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(this.isInvulnerableTo(source)) {
            return false;
        }
        else if(!this.world.isClient && this.isAlive()) {
            Entity trueSource = source.getSource();
            if(source instanceof ProjectileDamageSource && trueSource != null && this.isConnectedThroughVehicle(trueSource)) {
                return false;
            }
            else {
                /*if(Config.SERVER.vehicleDamage.get())
                {
                    this.setTimeSinceHit(10);
                    this.setHealth(this.getHealth() - amount);
                }*/
                boolean isCreativeMode = trueSource instanceof PlayerEntity && ((PlayerEntity) trueSource).isCreative();
                if(isCreativeMode || this.getHealth() < 0.0F) {
                    this.onVehicleDestroyed((LivingEntity) trueSource);
                    this.remove();
                }

                return true;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier) {
        if(/*Config.SERVER.vehicleDamage.get() && */distance >= 4F && this.getVelocity().getY() < -1.0F) {
            float damage = distance / 2F;
            this.damage(DamageSource.FALL, damage);
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.VEHICLE_IMPACT, SoundCategory.AMBIENT, 1.0F, 1.0F);
        }
        return true;
    }

    protected void onVehicleDestroyed(LivingEntity entity) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.VEHICLE_DESTROYED, SoundCategory.AMBIENT, 1.0F, 0.5F);

        boolean isCreativeMode = entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative();
        /*if(!isCreativeMode && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            VehicleRecipe recipe = VehicleRecipes.getRecipe(this.getType(), this.world);
            if(recipe != null)
            {
                List<ItemStack> materials = recipe.getMaterials();
                for(ItemStack stack : materials)
                {
                    ItemStack copy = stack.copy();
                    int shrink = copy.getCount() / 2;
                    if(shrink > 0)
                        copy.decrement(this.random.nextInt(shrink + 1));
                    InventoryUtil.spawnItemStack(this.world, this.getX(), this.getY(), this.getZ(), copy);
                }
            }
        }*/
    }

    public int getDestroyedStage()
    {
        return 10 - (int) Math.max(1.0F, (int) Math.ceil(10.0F * (this.getHealth() / this.getMaxHealth())));
    }

    /**
     * Smooths the rendering on servers
     */
    private void tickLerp() {
        /*if(this.canPassengerSteer()) {
            this.lerpSteps = 0;
            this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
        }*/

        if(this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double) this.yaw);
            this.yaw = (float) ((double) this.yaw + d3 / (double) this.lerpSteps);
            this.pitch = (float) ((double) this.pitch + (this.lerpPitch - (double) this.pitch) / (double) this.lerpSteps);
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRotation(this.yaw, this.pitch);
        }
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = yaw;
        this.lerpPitch = pitch;
        this.lerpSteps = 10;
    }

    @Override
    public void addPassenger(Entity passenger)
    {
        super.addPassenger(passenger);
        if(/*this.canPassengerSteer() && */this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.setPos(this.lerpX, this.lerpY, this.lerpZ);
            this.yaw = (float) this.lerpYaw;
            this.pitch = (float) this.lerpPitch;
        }
    }

    protected void applyYawToEntity(Entity passenger) {
        int seatIndex = this.getSeatTracker().getSeatIndex(passenger.getUuid());
        if(seatIndex != -1) {
            VehicleProperties properties = this.getProperties();
            Seat seat = properties.getSeats().get(seatIndex);
            passenger.setHeadYaw(this.getModifiedRotationYaw() + seat.getYawOffset());
            float f = MathHelper.wrapDegrees(passenger.yaw - this.getModifiedRotationYaw() + seat.getYawOffset());
            float f1 = MathHelper.clamp(f, -120.0F, 120.0F);
            passenger.prevYaw += f1 - f;
            passenger.yaw += f1 - f;
            passenger.setHeadYaw(passenger.yaw);
        }
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate)
    {
        this.applyYawToEntity(entityToUpdate);
    }*/

    @Override
    public void addVelocity(double x, double y, double z) {}

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit)
    {
        this.dataTracker.set(TIME_SINCE_HIT, timeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit()
    {
        return this.dataTracker.get(TIME_SINCE_HIT);
    }

    /**
     * Sets the max health of the vehicle.
     */
    public void setMaxHealth(float maxHealth)
    {
        this.dataTracker.set(MAX_HEALTH, maxHealth);
    }

    /**
     * Gets the max health of the vehicle.
     */
    public float getMaxHealth()
    {
        return this.dataTracker.get(MAX_HEALTH);
    }

    /**
     * Sets the current health of the vehicle.
     */
    public void setHealth(float health)
    {
        this.dataTracker.set(HEALTH, Math.min(this.getMaxHealth(), health));
    }

    /**
     * Gets the current health of the vehicle.
     */
    public float getHealth()
    {
        return this.dataTracker.get(HEALTH);
    }

    //TODO look into this and why its here. May have to send vanilla event to client
    @Override
    @Environment(EnvType.CLIENT)
    public void animateDamage() {
        this.setTimeSinceHit(10);
    }

    public boolean canBeColored()
    {
        return false;
    }

    public void setColor(int color) {
        if(this.canBeColored()) {
            this.dataTracker.set(COLOR, color);
        }
    }

    public void setColorRGB(int r, int g, int b) {
        int color = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
        this.dataTracker.set(COLOR, color);
    }

    public int getColor() {
        return this.dataTracker.get(COLOR);
    }

    public int[] getColorRGB() {
        int color = this.dataTracker.get(COLOR);
        return new int[]{ (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF };
    }

    public boolean canMountTrailer()
    {
        return true;
    }

    /**
     * Gets the absolute position of a part in the world
     *
     * @param position the position definition of the part
     * @return a Vec3d containing the exact location
     */
    public Vec3d getPartPositionAbsoluteVec(PartPosition position, float partialTicks)
    {
        VehicleProperties properties = this.getProperties();
        PartPosition bodyPosition = properties.getBodyPosition();
        Vec3d partVec = Vec3d.ZERO;
        partVec = partVec.add(0, 0.5, 0);
        partVec = partVec.multiply(position.getScale());
        partVec = partVec.add(0, -0.5, 0);
        partVec = partVec.add(position.getX() * 0.0625, position.getY() * 0.0625, position.getZ() * 0.0625);
        partVec = partVec.add(0, properties.getWheelOffset() * 0.0625, 0);
        partVec = partVec.add(0, properties.getAxleOffset() * 0.0625, 0);
        partVec = partVec.add(0, 0.5, 0);
        partVec = partVec.multiply(bodyPosition.getScale());
        partVec = partVec.add(0, -0.5, 0);
        partVec = partVec.add(0, 0.5, 0);
        partVec = partVec.add(bodyPosition.getX(), bodyPosition.getY(), bodyPosition.getZ());
        partVec = partVec.rotateY(-(this.prevYaw + (this.yaw - this.prevYaw) * partialTicks) * 0.017453292F);
        partVec = partVec.add(this.prevX + (this.getX() - this.prevX) * partialTicks, 0, 0);
        partVec = partVec.add(0, this.prevY + (this.getY() - this.prevY) * partialTicks, 0);
        partVec = partVec.add(0, 0, this.prevZ + (this.getZ() - this.prevZ) * partialTicks);
        return partVec;
    }

    protected static Box createScaledBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2, double scale) {
        return new Box(x1 * scale, y1 * scale, z1 * scale, x2 * scale, y2 * scale, z2 * scale);
    }

    /*@Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeFloat(this.rotationYaw);
        this.seatTracker.write(buffer);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.rotationYaw = this.prevRotationYaw = buffer.readFloat();
        this.seatTracker.read(buffer);
    }*/

    public boolean canTowTrailer()
    {
        return false;
    }

    public void setTrailer(TrailerEntity trailer) {
        if(trailer != null) {
            this.trailer = trailer;
            this.trailerId = trailer.getUuid();
            trailer.setPullingEntity(this);
            this.dataTracker.set(TRAILER, trailer.getEntityId());
        }
        else {
            if(this.trailer != null && this.trailer.getPullingEntity() == this) {
                this.trailer.setPullingEntity(null);
            }
            this.trailer = null;
            this.trailerId = null;
            this.dataTracker.set(TRAILER, -1);
        }
    }

    public UUID getTrailerId() {
        return trailerId;
    }

    public TrailerEntity getTrailer() {
        return trailer;
    }

    public VehicleProperties getProperties() {
        return VehicleProperties.getProperties(this.getType());
    }

    public float getModifiedRotationYaw() {
        return this.yaw;
    }

    /*@Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        Identifier entityId = this.getType().getRegistryName();
        if(entityId != null)
        {
            return BlockVehicleCrate.create(entityId, this.getColor(), null, null, -1);
        }
        return ItemStack.EMPTY;
    }*/

    @Override
    public Packet<?> createSpawnPacket() {
        return S2CEntitySpawnPacket.createPacket(this);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() < this.getProperties().getSeats().size();
    }

    /*@Override
    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);
        this.updatePassengerPosition(passenger);
    }*/

    public void updatePassengerPosition(Entity passenger) {
        if(this.isConnectedThroughVehicle(passenger)) {
            int seatIndex = this.getSeatTracker().getSeatIndex(passenger.getUuid());
            if(seatIndex != -1) {
                VehicleProperties properties = this.getProperties();
                if(seatIndex >= 0 && seatIndex < properties.getSeats().size()) {
                    Seat seat = properties.getSeats().get(seatIndex);
                    Vec3d seatVec = seat.getPosition().add(0, properties.getAxleOffset() + properties.getWheelOffset(), 0).multiply(properties.getBodyPosition().getScale()).rotateY(-this.getModifiedRotationYaw() * 0.017453292F - ((float) Math.PI / 2F));
                    passenger.setPos(this.getX() + seatVec.x, this.getY() + seatVec.y, this.getZ() + seatVec.z);
                    this.applyYawToEntity(passenger);
                }
            }
        }
    }

    public SeatTracker getSeatTracker()
    {
        return this.seatTracker;
    }

}
