public class Race {
    public static void main(String[] args) {

        TypingRace race = new TypingRace(40);

        Typist t1 = new Typist('①', "TURBOFINGERS", 0.85);
        Typist t2 = new Typist('②', "QWERTY_QUEEN", 0.60);
        Typist t3 = new Typist('③', "HUNT_N_PECK", 0.30);

        race.addTypist(t1, 1);
        race.addTypist(t2, 2);
        race.addTypist(t3, 3);

        race.startRace();
    }
}