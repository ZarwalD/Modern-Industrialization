/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.machines.recipe.condition;

import aztech.modern_industrialization.MIItem;
import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record DimensionProcessCondition(ResourceKey<Level> dimension) implements MachineProcessCondition {
    static final MapCodec<DimensionProcessCondition> CODEC = ResourceKey.codec(Registries.DIMENSION)
            .fieldOf("dimension")
            .xmap(DimensionProcessCondition::new, DimensionProcessCondition::dimension);

    static final StreamCodec<RegistryFriendlyByteBuf, DimensionProcessCondition> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION),
            DimensionProcessCondition::dimension,
            DimensionProcessCondition::new);

    @Override
    public boolean canProcessRecipe(Context context, MachineRecipe recipe) {
        return context.getLevel().dimension() == dimension;
    }

    @Override
    public void appendDescription(List<Component> list) {
        var loc = dimension.location();
        var dimComponent = Component.translatable("dimension.%s.%s".formatted(loc.getNamespace(), loc.getPath()));
        list.add(MIText.RequiresDimension.text(dimComponent));
    }

    @Override
    public ItemStack icon() {
        return MIItem.SINGULARITY.asItem().getDefaultInstance();
    }

    @Override
    public MapCodec<? extends MachineProcessCondition> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ? extends MachineProcessCondition> streamCodec() {
        return STREAM_CODEC;
    }
}
