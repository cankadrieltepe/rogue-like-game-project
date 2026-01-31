Rogue-Like (COMP 302) — Java Dungeon Game

A 2D rogue-like dungeon game built in Java for COMP 302 (Fall 2024). The game has two main modes:

Build Mode: design the dungeon halls by placing objects on a grid

Play Mode: play inside the dungeon you created, collect runes, use enchantments, and survive monsters

⏱ Designed for short sessions (typically under 10 minutes per run), making it easy to play during breaks.


Key Features

Build Mode

Create and configure 4 halls (Water, Earth, Air, Fire)

Place objects on a grid with game rules & limits

Save and start the dungeon you designed

Play Mode

Explore halls to find the hidden rune (required to unlock doors)

Timer-based challenge: if time runs out before finding the rune → Game Over

Lives system: take damage from monsters, lose hearts, and manage risk

Monsters

Fighter Monster: damages when adjacent

Archer Monster: damages when within range and/or by arrow hit

Wizard Monster (Phase 2): dynamic behavior based on remaining time

May teleport the player or teleport the rune periodically

Enchantments

Extra Time (instant)

Extra Life (instant)

Reveal (temporary hint)

Cloak of Protection (temporary protection)

Luring Gem (misleads monsters)


Game UX

Main menu: New Game / Help / Exit

Pause/Resume via ESC

Game Over & Victory screens with Restart / Exit

Help screen describing controls & gameplay

Controls

Arrow keys: move hero (North / South / East / West)

Mouse: interact in build mode + click/search objects

Right click: collect enchantments / interact (depending on the object)

ESC: pause/resume



Project Context (University Work)

This repository is a public showcase copy of our final COMP 302 project submission:

Course: COMP 302

Term: Fall 2024

Project type: Group project (6 people)

Team

Group 16: OrtayaKarshisik

Mert Erdem

Reşit Mehmet Ayhan

Çağatay Cingöz

Can Kadri Eltepe

Emre Kıyak

Sinem Taşçı


This repo is shared for portfolio purposes. Credit belongs to the full team.
