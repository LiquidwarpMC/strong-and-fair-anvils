# Strong and Fair Anvils

Modifies regular anvils:

- No random degradation from usage (they can still degrade through falling though)
- Name change operations always have no level cost
- Any operation on or between unenchanted items has no level cost (repair with materials, repair via merging)
- Any operation changing an item's enchantments (merging with an enchanted book or another enchanted item) increases prior work penalty. This is the only operation that increases prior work penalty (in vanilla, any operation increases it)
- Any operation on enchanted items, including repair with materials, will cost levels same as vanilla: prior work penalty + per-material cost. However it will not increase prior work penalty.


Also adds a Stone Anvil that can only perform operations that do not cost experience levels (working on unenchanted items or renaming only).

It can be crafted with this recipe:

[DDD]
[ S ]
[SSS]

with D = Polished Diorite (Vanilla) and S = Smooth Stone (Vanilla)

Requires Fabric API.
