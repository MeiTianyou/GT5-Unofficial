package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static goodgenerator.loader.Loaders.advancedRadiationProtectionPlate;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.block.BlockQuantumGlass;

public class RecipeLoaderChemicalSkips {

    public static void generate() {
        createRecipes();
    }

    private static void createRecipes() {
        quantumTransformerRecipes();
        fusionReactorRecipes();
        catalystRecipes();
        tieredCasingRecipes();
    }

    // All the recipes that the QFT can do. Each recipe has a machine tier.
    // -> Tier 1 is UEV (UEV circuits and 1 Eternal Singularity);
    // -> Tier 2 needs new item from QFT, plus stacks of Infinity;
    // -> Tier 3 needs new item from QFT, plus stacks of Transcendent Metal;
    // -> Tier 4 needs new item from QFT, plus stacks of Spacetime;
    // (Until they are created, the new items are represented by
    // HSS-G for Tier 2, HSS-S for Tier 3 and HSS-E for Tier 4)

    private static void quantumTransformerRecipes() {
        ItemStack stemcells = GTUtility.copyAmountUnsafe(64 * 32, ItemList.Circuit_Chip_Stemcell.get(1));
        ItemStack biocells = GTUtility.copyAmountUnsafe(64 * 32, ItemList.Circuit_Chip_Biocell.get(1));
        // Platline
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.PTMetallicPowder.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Platinum.getDust(64),
                Materials.Palladium.getDust(64),
                Materials.Iridium.getDust(64),
                Materials.Osmium.getDust(64),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Partial platline (from Pd, Os, Ir, Rh and leach)
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Palladium.getDust(64),
                Materials.Platinum.getDust(64),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.IrLeachResidue.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Iridium.getDust(64),
                Materials.Platinum.getDust(64),
                Materials.Osmiridium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.IrOsLeachResidue.get(OrePrefixes.dust, 32))
            .itemOutputs(Materials.Osmium.getDust(64), Materials.Iridium.getDust(64), Materials.Osmiridium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.CrudeRhMetall.get(OrePrefixes.dust, 32))
            .itemOutputs(
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                Materials.Palladium.getDust(64),
                Materials.Platinum.getDust(64),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.LeachResidue.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Iridium.getDust(64),
                Materials.Osmium.getDust(64),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Plastics
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64))
            .fluidInputs(
                Materials.Oxygen.getGas(1000 * 16),
                Materials.Hydrogen.getGas(1000 * 16),
                Materials.Chlorine.getGas(1000 * 16),
                Materials.Fluorine.getGas(1000 * 16))
            .fluidOutputs(
                Materials.Plastic.getMolten(144 * 256),
                Materials.PolyvinylChloride.getMolten(144 * 128),
                Materials.Polystyrene.getMolten(144 * 64),
                Materials.Polytetrafluoroethylene.getMolten(144 * 128),
                Materials.Epoxid.getMolten(144 * 64),
                Materials.Polybenzimidazole.getMolten(144 * 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.PlasticPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Rubbers/Cable Materials
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64))
            .fluidInputs(
                Materials.Oxygen.getGas(1000 * 16),
                Materials.Hydrogen.getGas(1000 * 16),
                Materials.Chlorine.getGas(1000 * 16))
            .fluidOutputs(
                Materials.Silicone.getMolten(144 * 64),
                Materials.StyreneButadieneRubber.getMolten(144 * 64),
                Materials.PolyphenyleneSulfide.getMolten(144 * 128),
                Materials.Rubber.getMolten(144 * 256))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.RubberPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .noBuffer()
            .addTo(quantumForceTransformerRecipes);
        // Glues and Solders
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(32), Materials.Bismuth.getDust(32))
            .itemOutputs(ItemList.StableAdhesive.get(1))
            .fluidInputs(Materials.Oxygen.getGas(10000), Materials.Hydrogen.getGas(10000))
            .fluidOutputs(
                MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(1000 * 32),
                Materials.AdvancedGlue.getFluid(1000 * 16),
                MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 64),
                Materials.SolderingAlloy.getMolten(144 * 128))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.AdhesionPromoterCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Titanium, Tungsten, Indium
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Lead.getDust(16), Materials.Bauxite.getDust(32), Materials.Tungstate.getDust(16))
            .itemOutputs(
                Materials.Titanium.getDust(64),
                Materials.TungstenSteel.getDust(64),
                Materials.TungstenCarbide.getDust(64),
                Materials.Indium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Rutile.getDust(32), Materials.Scheelite.getDust(16), Materials.Ilmenite.getDust(16))
            .itemOutputs(
                Materials.Titanium.getDust(64),
                Materials.TungstenSteel.getDust(64),
                Materials.Tantalum.getDust(64),
                Materials.Indium.getDust(64),
                Materials.Niobium.getDust(64),
                MaterialsElements.getInstance().HAFNIUM.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Thorium, Uranium, Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Thorium.getDust(32), Materials.Uranium.getDust(32))
            .itemOutputs(
                MaterialsElements.getInstance().THORIUM232.getDust(64),
                MaterialsElements.getInstance().URANIUM233.getDust(64),
                Materials.Uranium235.getDust(64),
                MaterialsElements.getInstance().PLUTONIUM238.getDust(64),
                Materials.Plutonium.getDust(64),
                Materials.Plutonium241.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.RadioactivityCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Monaline
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Monazite.getDust(32))
            .itemOutputs(
                Materials.Cerium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Samarium.getDust(64),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 11002),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 11007),
                ItemList.SuperconductorComposite.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Bastnasite.getDust(32))
            .itemOutputs(
                Materials.Holmium.getDust(64),
                Materials.Cerium.getDust(64),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Lanthanum.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline from Cerium-rich mixture
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 16))
            .itemOutputs(
                Materials.Holmium.getDust(64),
                Materials.Cerium.getDust(64),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Lanthanum.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        if (EtFuturumRequiem.isModLoaded()) {
            // Netherite
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility
                        .copyAmountUnsafe(64 * 16, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1)),
                    getModItem(EtFuturumRequiem.ID, "ancient_debris", 4))
                .fluidInputs(
                    Materials.NetherAir.getFluid(64000),
                    Materials.HellishMetal.getMolten(8 * 144),
                    FluidUtils.getLava(256000))
                .itemOutputs(
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 16),
                    ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(64))
                .fluidOutputs(Materials.NefariousOil.getFluid(64000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(QFT_CATALYST, GregtechItemList.HellishForceCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 2)
                .addTo(quantumForceTransformerRecipes);
        }
        // Stem Cells
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Calcium.getDust(32),
                Materials.MeatRaw.getDust(32),
                getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 32, 2))
            .itemOutputs(stemcells)
            .fluidOutputs(
                Materials.GrowthMediumRaw.getFluid(1000 * 1024),
                Materials.GrowthMediumSterilized.getFluid(1000 * 512))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_CATALYST, GregtechItemList.RawIntelligenceCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Unknown Particles
        GTValues.RA.stdBuilder()
            .itemOutputs(
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.GRAVITON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.ELECTRON))
            .fluidInputs(Materials.Hydrogen.getGas(10000L), Materials.Deuterium.getGas(1000L))
            .fluidOutputs(FluidUtils.getFluidStack("plasma.hydrogen", 1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_CATALYST, GregtechItemList.ParticleAccelerationCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Lategame Plastics (Missing Radox Polymer and Heavy Radox)
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64), Materials.Osmium.getDust(24))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 16), Materials.Nitrogen.getGas(1000 * 16))
            .fluidOutputs(
                FluidUtils.getFluidStack("xenoxene", 1000 * 16),
                FluidUtils.getFluidStack("molten.radoxpoly", 144 * 64),
                FluidUtils.getFluidStack("heavyradox", 1000 * 16),
                MaterialsKevlar.Kevlar.getMolten(144 * 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.UltimatePlasticCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        if (Mods.Forestry.isModLoaded()) {
            // Lategame Kevlar using Kevlar bee comb
            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.KEVLAR, 24), Materials.Carbon.getDust(64))
                .fluidInputs(Materials.Nitrogen.getGas(1000 * 16), Materials.Hydrogen.getGas(1000 * 16))
                .fluidOutputs(
                    MaterialsKevlar.PolyurethaneResin.getFluid(1000 * 32),
                    MaterialsKevlar.LiquidCrystalKevlar.getFluid(144 * 32),
                    MaterialsKevlar.Kevlar.getMolten(144 * 64))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .metadata(QFT_CATALYST, GregtechItemList.UltimatePlasticCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 4)
                .addTo(quantumForceTransformerRecipes);
            // Platline skip using Platline Combs (Palladium, Osmium, Iridium, Platinum)
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTBees.combs.getStackForType(CombType.PLATINUM, 32),
                    GTBees.combs.getStackForType(CombType.PALLADIUM, 32),
                    GTBees.combs.getStackForType(CombType.OSMIUM, 32),
                    GTBees.combs.getStackForType(CombType.IRIDIUM, 32))
                .fluidOutputs(
                    Materials.Osmium.getMolten(144 * 256),
                    Materials.Palladium.getMolten(144 * 256),
                    Materials.Iridium.getMolten(144 * 256),
                    Materials.Platinum.getMolten(144 * 256))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 1)
                .addTo(quantumForceTransformerRecipes);
        }
        // Bio Cells and Mutated Solder
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Chip_Stemcell.get(16), Materials.InfinityCatalyst.getDust(4))
            .itemOutputs(biocells)
            .fluidOutputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.BioMediumSterilized.getFluid(1000 * 256),
                Materials.BioMediumRaw.getFluid(1000 * 512))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.BiologicalIntelligenceCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        // Rare Particles
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Laser_Lens_Special.get(1))
            .itemOutputs(
                Particle.getBaseParticle(Particle.Z_BOSON),
                Particle.getBaseParticle(Particle.W_BOSON),
                Particle.getBaseParticle(Particle.LAMBDA),
                Particle.getBaseParticle(Particle.OMEGA),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .fluidInputs(
                FluidUtils.getFluidStack("plasma.hydrogen", 30000),
                Materials.Helium.getPlasma(30000L),
                Materials.Americium.getPlasma(30000L),
                new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 30000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.SynchrotronCapableCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);

        if (GalaxySpace.isModLoaded()) {
            // Seaweed
            ItemStack seaweed = GTUtility
                .copyAmountUnsafe(64 * 32, getModItem(GalaxySpace.ID, "tcetiedandelions", 1, 4));
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("cropSeaweed", 64), Materials.Mytryl.getDust(16))
                .itemOutputs(seaweed, getModItem(NewHorizonsCoreMod.ID, "item.TCetiESeaweedExtract", 16))
                .fluidInputs(FluidUtils.getFluidStack("unknowwater", 25_000))
                .fluidOutputs(
                    FluidUtils.getFluidStack("seaweedbroth", 50_000),
                    FluidUtils.getFluidStack("iodine", 64_000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .metadata(QFT_CATALYST, GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 4)
                .addTo(quantumForceTransformerRecipes);

        }
    }

    private static void fusionReactorRecipes() {
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Radon.getPlasma(100), Materials.Nitrogen.getPlasma(100))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Americium.getPlasma(100), Materials.Boron.getPlasma(100))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        // MK5 versions
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 576),
                Materials.Yttrium.getMolten(576))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 576))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 576),
                Materials.Rubidium.getMolten(576))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 576))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);
    }

    private static void catalystRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 88),
                Materials.Osmiridium.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(GregtechItemList.PlatinumGroupCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Polybenzimidazole.getDust(64),
                Materials.Polytetrafluoroethylene.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(GregtechItemList.PlasticPolymerCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Silicone.getDust(64),
                Materials.StyreneButadieneRubber.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(GregtechItemList.RubberPolymerCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                MaterialsAlloy.INDALLOY_140.getDust(64),
                MaterialMisc.ETHYL_CYANOACRYLATE.getCell(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(GregtechItemList.AdhesionPromoterCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.TungstenSteel.getDust(64),
                Materials.Indium.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(GregtechItemList.TitaTungstenIndiumCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                MaterialsElements.getInstance().URANIUM235.getDust(64),
                MaterialsElements.getInstance().PLUTONIUM241.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(GregtechItemList.RadioactivityCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Silver.getNanite(1))
            .itemOutputs(GregtechItemList.RareEarthGroupCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(9216))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Naquadah.getDust(64),
                Materials.Adamantium.getDust(64),
                Materials.Silver.getNanite(1))
            .itemOutputs(GregtechItemList.SimpleNaquadahCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(9216))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        if (EtFuturumRequiem.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    CI.getEmptyCatalyst(1),
                    Materials.Netherite.getDust(64),
                    Materials.InfusedGold.getDust(64),
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 1),
                    Materials.Neutronium.getNanite(1))
                .itemOutputs(GregtechItemList.HellishForceCatalyst.get(1))
                .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(9216))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(assemblerRecipes);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Naquadria.getDust(64),
                Materials.Trinium.getDust(64),
                Materials.Gold.getNanite(1))
            .itemOutputs(GregtechItemList.AdvancedNaquadahCatalyst.get(1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(9216L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                ItemList.Circuit_Chip_Stemcell.get(64),
                Materials.Gold.getNanite(1))
            .itemOutputs(GregtechItemList.RawIntelligenceCatalyst.get(1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(9216L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                GregtechItemList.Laser_Lens_Special.get(64),
                GTModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 10))
            .itemOutputs(GregtechItemList.ParticleAccelerationCatalyst.get(1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(9216L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                MaterialsKevlar.Kevlar.getDust(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(1))
            .itemOutputs(GregtechItemList.UltimatePlasticCatalyst.get(1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                ItemList.Circuit_Chip_Biocell.get(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(1))
            .itemOutputs(GregtechItemList.BiologicalIntelligenceCatalyst.get(1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Particle.getBaseParticle(Particle.HIGGS_BOSON),
                Particle.getIon("Helium", 0),
                Particle.getIon("Hydrogen", 0),
                MaterialsUEVplus.Eternity.getNanite(16))
            .itemOutputs(GregtechItemList.SynchrotronCapableCatalyst.get(1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                GTOreDictUnificator.get("blockShirabon", 16),
                MaterialsUEVplus.Universium.getNanite(1),
                ItemList.Timepiece.get(1))
            .itemOutputs(GregtechItemList.TemporalHarmonyCatalyst.get(1))
            .fluidInputs(Materials.DarkIron.getMolten(92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                getModItem(NewHorizonsCoreMod.ID, "item.TCetiESeaweedExtract", 64),
                GTOreDictUnificator.get("dustIodine", 64),
                MaterialsUEVplus.TranscendentMetal.getNanite(1))
            .itemOutputs(GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92_160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
    }

    private static void tieredCasingRecipes() {
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.ForceFieldGlass.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(1), Materials.Carbon.getNanite(4),
                ItemList.Emitter_UV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 2) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 10), MaterialsUEVplus.ExcitedDTCC.getFluid(5000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 500),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 500) },
            GregtechItemList.NeutronPulseManipulator.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronPulseManipulator.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(2), Materials.Carbon.getNanite(8),
                ItemList.Emitter_UEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 4),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 12), MaterialsUEVplus.ExcitedDTPC.getFluid(5000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 2500),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 2500) },
            GregtechItemList.CosmicFabricManipulator.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricManipulator.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(4), Materials.Carbon.getNanite(16),
                ItemList.Emitter_UIV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 8),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 15), MaterialsUEVplus.ExcitedDTRC.getFluid(5000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 1000 * 10),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 1000 * 10) },
            GregtechItemList.InfinityInfusedManipulator.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedManipulator.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(8), Materials.Carbon.getNanite(32),
                ItemList.Emitter_UMV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 20), MaterialsUEVplus.ExcitedDTEC.getFluid(5000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 2000 * 10),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 2000 * 10) },
            GregtechItemList.SpaceTimeContinuumRipper.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UXV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_AdvancedRadiationProof.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(1),
                GTOreDictUnificator.get("plateDensePreciousMetalsAlloy", 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 2),
                ItemList.Field_Generator_UV.get(1), MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getScrew(16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 10), },
            GregtechItemList.NeutronShieldingCore.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronShieldingCore.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(2),
                GTOreDictUnificator.get("plateDenseEnrichedNaquadahAlloy", 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 2),
                ItemList.Field_Generator_UEV.get(1),
                // Radox polymer screw.
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.get("RadoxPoly"), 16),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 20), },
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_UHV,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(4),
                MaterialsElements.STANDALONE.HYPOGEN.getPlateDense(4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.ProtoHalkonite, 2),
                ItemList.Field_Generator_UIV.get(1), GTOreDictUnificator.get("screwMetastableOganesson", 16),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 40), },
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            8192 * 30 * 20,
            8192,
            (int) TierEU.RECIPE_UEV,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(8), GTOreDictUnificator.get("plateDenseShirabon", 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.SpaceTime, 2),
                ItemList.Field_Generator_UMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Dilithium, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 80), },
            GregtechItemList.SpaceTimeBendingCore.get(1),
            120 * 20,
            (int) TierEU.RECIPE_UXV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(BlockQuantumGlass.INSTANCE, 1),
                ItemList.Field_Generator_ZPM.get(1),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getLongRod(6),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlate(6))
            .itemOutputs(GregtechItemList.ForceFieldGlass.get(1))
            .fluidInputs(MaterialsAlloy.QUANTUM.getFluidStack(144 * 6))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

    }
}
