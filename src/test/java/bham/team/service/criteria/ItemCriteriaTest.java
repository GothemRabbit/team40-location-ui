package bham.team.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ItemCriteriaTest {

    @Test
    void newItemCriteriaHasAllFiltersNullTest() {
        var itemCriteria = new ItemCriteria();
        assertThat(itemCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void itemCriteriaFluentMethodsCreatesFiltersTest() {
        var itemCriteria = new ItemCriteria();

        setAllFilters(itemCriteria);

        assertThat(itemCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void itemCriteriaCopyCreatesNullFilterTest() {
        var itemCriteria = new ItemCriteria();
        var copy = itemCriteria.copy();

        assertThat(itemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(itemCriteria)
        );
    }

    @Test
    void itemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var itemCriteria = new ItemCriteria();
        setAllFilters(itemCriteria);

        var copy = itemCriteria.copy();

        assertThat(itemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(itemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var itemCriteria = new ItemCriteria();

        assertThat(itemCriteria).hasToString("ItemCriteria{}");
    }

    private static void setAllFilters(ItemCriteria itemCriteria) {
        itemCriteria.id();
        itemCriteria.title();
        itemCriteria.price();
        itemCriteria.condition();
        itemCriteria.category();
        itemCriteria.brand();
        itemCriteria.colour();
        itemCriteria.timeListed();
        itemCriteria.imagesId();
        itemCriteria.wishlistId();
        itemCriteria.productStatusId();
        itemCriteria.profileDetailsId();
        itemCriteria.likesId();
        itemCriteria.sellerId();
        itemCriteria.distinct();
    }

    private static Condition<ItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getCondition()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getBrand()) &&
                condition.apply(criteria.getColour()) &&
                condition.apply(criteria.getTimeListed()) &&
                condition.apply(criteria.getImagesId()) &&
                condition.apply(criteria.getWishlistId()) &&
                condition.apply(criteria.getProductStatusId()) &&
                condition.apply(criteria.getProfileDetailsId()) &&
                condition.apply(criteria.getLikesId()) &&
                condition.apply(criteria.getSellerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ItemCriteria> copyFiltersAre(ItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getCondition(), copy.getCondition()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getBrand(), copy.getBrand()) &&
                condition.apply(criteria.getColour(), copy.getColour()) &&
                condition.apply(criteria.getTimeListed(), copy.getTimeListed()) &&
                condition.apply(criteria.getImagesId(), copy.getImagesId()) &&
                condition.apply(criteria.getWishlistId(), copy.getWishlistId()) &&
                condition.apply(criteria.getProductStatusId(), copy.getProductStatusId()) &&
                condition.apply(criteria.getProfileDetailsId(), copy.getProfileDetailsId()) &&
                condition.apply(criteria.getLikesId(), copy.getLikesId()) &&
                condition.apply(criteria.getSellerId(), copy.getSellerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
