export class RandomNameGenerator {
    static generateRandomName() {
        let name: string = '';
        for (let i = 0; i < 3; i++) {
            name += Math.random()
                .toString(36)
                .substring(7);
        }
        return name.toUpperCase();
    }
}
