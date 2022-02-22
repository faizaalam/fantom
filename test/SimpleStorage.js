const SimpleStorage = artifacts.require('SimpleStorage.sol');


contract('SimpleStorage', () => {
    it('Should update data', async () => {
        const storage = await SimpleStorage.new();
        await storage.set(10);
        const data = await storage.get();
        assert(data.toString() === '10');
    })
});