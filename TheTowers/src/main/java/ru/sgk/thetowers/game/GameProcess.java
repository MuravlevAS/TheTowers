package ru.sgk.thetowers.game;

import ru.sgk.thetowers.game.data.arenas.GameArena;
import ru.sgk.thetowers.game.data.arenas.GameArenas;
import ru.sgk.thetowers.utils.GameScheduler;

public class GameProcess {
	/** id потока игрового процесса */
	private int threadId;
	/** Главный счётчик игрового процесса (в мс) */
	private int mainCounter;
	/** Количество секунд, которые идёт игра */
	private int seconds;
	/** Количество минут, которые идёт игра */
	private int minutes;
	/** Промежуточный таймер. Нужен для всякой херни */
	private int tmpTimer;
	/** Включён ли промежуточный таймер */
	private boolean tmpTimerOn;
	/** Арена, на которой идёт игровой процесс */
	private GameArena arena;
	/** Начата ли игра. Т.е истёк ли таймер отсрочки до начала игры*/
	private boolean started;
	/** Время (в мс), после истечения которого начинается игра. */
	private int startedTime;
	
	/**
	 * Создаёт игровой процесс относящийся к арене <i>arena</i>
	 */
	public GameProcess(String arena)
	{
		this.arena = GameArenas.getArena(arena);
		this.startedTime = 10_000;
	}
	/**
	 * запускает игровой процесс.
	 */
	public void start()
	{
		// обнуляем всех переменных  
		tmpTimer = 0;
		mainCounter = 0;
		seconds = 0;

		started = false;
		threadId = GameScheduler.scheduleTask(this::update, 0);
	}

	/**
	 * Останавливает игровой процесс
	 */
	public void stop()
	{
		// TODO Остановка игрового процесса
		GameScheduler.cancel(threadId);
		
	}

	/**
	 * repeated once per second
	 */
	private void update()
	{
		// counter прибавляется каждую мс. При достижении 1к, секунды увеличиваются на 1
		if (mainCounter % 1000 == 0)
		{
			seconds++;
			mainCounter = 0;
		}
		// Если игра ещё не начата
		if (!started)
		{
			// И таймер больше чем время старта, стартуем игру
			if (mainCounter >= startedTime) started = true;
		}
		else
		{
			// TODO: game process;
		}
		
		// Обновление счётчиков
		mainCounter++;
		if (tmpTimerOn) tmpTimer++;
	}
	/**
	 * 
	 * @return глобальный счётчик игры.
	 */

}
