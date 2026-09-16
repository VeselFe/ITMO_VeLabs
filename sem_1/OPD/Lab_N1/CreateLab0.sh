#!/bin/bash

chmod -R 700 lab0
rm -r lab0
mkdir -p lab0
cd lab0

touch cleffa5

mkdir -p magnemite0/swellow/arbok
touch magnemite0/swellow/sandile
mkdir -p magnemite0/vibrava/sandslash
touch magnemite0/vibrava/walrein
mkdir -p magnemite0/zubat/buizel
touch magnemite0/zubat/azumarill
mkdir -p magnemite0/zubat/hitmonlee
touch magnemite0/zubat/beartic
touch magnemite0/{persian,abra,shelmet}

touch mareep1
touch pupitar7

mkdir -p shroomish8/venusaur
touch shroomish8/venusaur/tepig
mkdir -p shroomish8/{venusaur/duosion,darmanitan,lampent}
mkdir -p shroomish8/darmanitan/{anorith,dragonite}
touch shroomish8/darmanitan/{magcargo,yanmega,marill,staravia,cherrim}
mkdir -p shroomish8/blaziken
touch shroomish8/blaziken/carracosta
mkdir -p shroomish8/blaziken/{simisear,scolipede}
touch shroomish8/lampent/pineco
mkdir -p shroomish8/lampent/{dwebble,machop,kingdra}
touch shroomish8/axew

mkdir -p victreebel7
touch victreebel7/{hydreigon,venonat,whiscash,spoink}
mkdir -p victreebel7/buizel/{quilava,magmortar}

# ввод содержимого
cat > cleffa5 << "EOF"
cleffa5: Способности Charm Pound Encore Sing Sweet Kiss Copycat
Magical Leaf
EOF

cat > magnemite0/swellow/sandile << "EOF"
weigth=33.5 height=28.0 atk=7 def=4
EOF

cat > magnemite0/vibrava/walrein << "EOF"
Тип
покемона ІCE WATER
EOF

echo "Живет Freshwater" > magnemite0/zubat/azumarill 

cat > magnemite0/zubat/beartic << "EOF"
satk=7
sdef=8 spd=5
EOF

cat > magnemite0/persian << "EOF"
Тип диеты Carnivore
EOF

cat > magnemite0/abra << "EOF"
Тип покемона PSYCHIC
NONE
EOF

cat > magnemite0/shelmet << "EOF"
Способности Swarm Hydration Shell Armor
EOF

cat > mareep1 << "EOF"
satk=7
sdef=5 spd=4
EOF

cat > pupitar7 << "EOF"
weigth=335.1 height=47.0 atk=8 def=7
tepig:
Тип
покемона FIRE NONE
EOF

cat >> shroomish8/darmanitan/cherrim << "EOF"
Возможности Overland=6 Surface=4 Jump=4
Power=1 Intelligence=4 Bloom=0
EOF

cat > shroomish8/darmanitan/magcargo << "EOF"
Развитые способности Weak
Armor
EOF

cat > shroomish8/darmanitan/yanmega << "EOF"
Развитые способности Frisk
Compoundeyes
EOF

cat > shroomish8/darmanitan/marill << "EOF"
Способности Torrent Huge Power Thick
Fat
EOF

cat > shroomish8/darmanitan/staravia << "EOF"
Способности Quick Attack Wing Attack Double Team
Endeavor Whirlwind Aerial Ace Take Down Agility Brave Bird Final 
Gambit
EOF

cat > shroomish8/blaziken/carracosta << "EOF"
Способности Torrent Mountain Peak Solid Rock
Study
EOF

cat > shroomish8/lampent/pineco << "EOF"
Возможности Overland=3 Jump=4 Power=1 Intelligence=3
Sinker=0 Threaded=0
EOF

cat > shroomish8/axew << "EOF"
Способности Scratch Leer Assurance Dragon
Rage Double Chop Scary Face Slash False Swipe Dragon Claw Dragon Dance
Taunt Dragon Pulse Swords Dance Guillotine Outrage Giga
Impact
EOF

cat > victreebel7/hydreigon << "EOF"
Hyper
Развитые способности Нурer Cutter
EOF

cat > victreebel7/venonat << "EOF"
Развитые
способности Run Away
EOF

cat > victreebel7/whiscash << "EOF"
Развитые способности
Hydration
EOF

cat > victreebel7/spoink << "EOF"
Способности Splash Psywave Odor Sleuth Psybeam
Psych Up Confuse Ray Magic Coat Zen Headbutt Rest Snore Power Gem 
Psyshock Payback Psychic Bounce
EOF

# Установление прав пользователей
chmod u=rw,g=,o= cleffa5
chmod u=wx,g=x,o=w magnemite0
chmod 537 magnemite0/swellow
chmod 764 magnemite0/swellow/arbok
chmod 004 magnemite0/swellow/sandile
chmod 500 magnemite0/vibrava
chmod 777 magnemite0/vibrava/sandslash
chmod 400 magnemite0/vibrava/walrein
chmod 777 magnemite0/zubat
chmod u=rx,g=x,o=wx magnemite0/zubat/buizel
chmod u=,g=r,o=rw magnemite0/zubat/azumarill
chmod 576 magnemite0/zubat/hitmonlee
chmod 440 magnemite0/persian
chmod u=r,g=r,o=r magnemite0/abra
chmod 044 magnemite0/shelmet
chmod u=rw,g=w,o= mareep1
chmod 620 pupitar7
chmod 511 shroomish8
chmod u=rx,g=rwx,o= shroomish8/venusaur
chmod 620 shroomish8/venusaur/tepig
chmod 375 shroomish8/venusaur/duosion
chmod 361 shroomish8/darmanitan
chmod 044 shroomish8/darmanitan/cherrim
chmod 753 shroomish8/darmanitan/anorith
chmod 440 shroomish8/darmanitan/magcargo
chmod 444 shroomish8/darmanitan/yanmega
chmod 573 shroomish8/darmanitan/dragonite
chmod u=rw,g=r,o= shroomish8/darmanitan/marill
chmod u=rw,g=,o= shroomish8/darmanitan/staravia
chmod 753 shroomish8/blaziken
chmod 620 shroomish8/blaziken/carracosta
chmod u=rx,g=rwx,o=wx shroomish8/blaziken/simisear
chmod u=wx,g=,o=rwx shroomish8/blaziken/scolipede
chmod 573 shroomish8/lampent
chmod u=rw,g=rw,o= shroomish8/lampent/pineco
chmod 733 shroomish8/lampent/dwebble
chmod 373 shroomish8/lampent/machop
chmod 375 shroomish8/lampent/kingdra
chmod u=,g=rw,o=r shroomish8/axew
chmod 577 victreebel7
chmod u=,g=,o=rw victreebel7/hydreigon
chmod 006 victreebel7/venonat
chmod u=rw,g=rw,o=r victreebel7/whiscash
chmod u=r,g=,o= victreebel7/spoink
chmod u=rx,g=rx,o=rx magnemite0/zubat/buizel
chmod u=rwx,g=rwx,o= victreebel7/buizel/quilava
chmod 753 victreebel7/buizel/magmortar


# Копирование и создание ссылок
ln -s victreebel7 Copy_3                        #done
ln cleffa5 magnemite0/shelmetcleffa             #done

chmod a=wx victreebel7
ln -s pupitar7 victreebel7/hydreigonpupitar     #done

chmod -R 500 shroomish8
chmod u+w shroomish8/lampent/kingdra
cp -r shroomish8 victreebel7/buizel             #done

ln -s cleffa5 magnemite0/zubat/bearticcleffa    #done
ln -f cleffa5 magnemite0/shelmetcleffa          #done

chmod 300 shroomish8
chmod 300 shroomish8/lampent
touch shroomish8/lampent/pinecopupitar
cp pupitar7 shroomish8/lampent/pinecopupitar    #done
# возвращаю права назад
chmod 511 shroomish8
chmod 573 shroomish8/lampent

cp pupitar7 victreebel7/buizel/magmortar        #done
cp pupitar7 shroomish8/lampent/kingdra          #done 
cp pupitar7 victreebel7/buizel/magmortar        #done

chmod -R  700 magnemite0
touch magnemite0/zubat/cleffa5
cp -r magnemite0 ../magnemite0_copy
cp -r ../magnemite0_copy magnemite0/vibrava/sandslash #done
cp mareep1 magnemite0/zubat/azumarillmareep     #done
rm -r ../magnemite0_copy

chmod 400 victreebel7/hydreigon 
cat magnemite0/zubat/beartic victreebel7/hydreigon magnemite0/zubat/azumarill > mareep1_98 # done
ln -s magnemite0 Copy_36                        #done

# возвращаю права назад
chmod 312 magnemite0
chmod 537 magnemite0/swellow
chmod 764 magnemite0/swellow/arbok
chmod 004 magnemite0/swellow/sandile
chmod 500 magnemite0/vibrava
chmod 777 magnemite0/vibrava/sandslash
chmod 400 magnemite0/vibrava/walrein
chmod 777 magnemite0/zubat
chmod 613 magnemite0/zubat/buizel
chmod 046 magnemite0/zubat/azumarill
chmod 576 magnemite0/zubat/hitmonlee
chmod 440 magnemite0/persian
chmod 444 magnemite0/abra
chmod 044 magnemite0/shelmet
# возвращаю права назад
chmod 577 victreebel7
chmod 511 shroomish8
chmod u=rx,g=rwx,o= shroomish8/venusaur
chmod 620 shroomish8/venusaur/tepig
chmod 375 shroomish8/venusaur/duosion
chmod 361 shroomish8/darmanitan
chmod 044 shroomish8/darmanitan/cherrim
chmod 753 shroomish8/darmanitan/anorith
chmod 440 shroomish8/darmanitan/magcargo
chmod 444 shroomish8/darmanitan/yanmega
chmod 573 shroomish8/darmanitan/dragonite
chmod u=rw,g=r,o= shroomish8/darmanitan/marill
chmod u=rw,g=,o= shroomish8/darmanitan/staravia
chmod 753 shroomish8/blaziken
chmod 620 shroomish8/blaziken/carracosta
chmod u=rx,g=rwx,o=wx shroomish8/blaziken/simisear
chmod u=wx,g=,o=rwx shroomish8/blaziken/scolipede
chmod 573 shroomish8/lampent
chmod u=rw,g=rw,o= shroomish8/lampent/pineco
chmod 733 shroomish8/lampent/dwebble
chmod 373 shroomish8/lampent/machop
chmod 375 shroomish8/lampent/kingdra
chmod u=,g=rw,o=r shroomish8/axew


# Поиск и фильтрация 
#1
echo "#1"
wc -l victreebel7/* 2>&1 | sort -n
echo " "

#2
echo "#2"
cd ..
ls -lRt lab0 2>/dev/null | grep "pi" | tail -n 4
echo " "

#3
echo "#3"
touch /tmp/errors.log
cd lab0
cat victreebel7/* 2>/tmp/errors.log | grep -v 'E$'
echo " " >> /tmp/errors.log
echo " "

#4
echo "#4"
chmod -R 500 magnemite0
grep -i -v "ck" -n -r magnemite0 2>&1
echo " "

#5
echo "#5"
cat victreebel7/* 2>&1 | grep 'n$'
echo " "


#6
echo "#6"
cd ..
echo "#6" >> /tmp/errors.log
wc -l lab0/* lab0/*/* lab0/*/*/* 2>> /tmp/errors.log | grep "n$" | sort -nr
echo " " >> /tmp/errors.log
echo " "

#7
echo "#7"
cat m* */m* */*/m* 2>/dev/null | sort -r
echo " "

#8
echo "#8"
cd lab0
ls -lR 2>&1 | grep "h$" | sort
echo " "

#9
echo "#9"
echo "#9" >> /tmp/errors.log
ls -lRtr 2>>/tmp/errors.log | grep "w$"
echo " " >> /tmp/errors.log
echo " "

#10
echo "#10"
echo "#10" >> /tmp/errors.log
ls -lR 2>>/tmp/error.log | sort -k5 -nr | head -n 2
echo " " >> /tmp/errors.log
echo " "

#11
echo "#11"
ls magnemite0 2>&1 | sort
echo " "

#12
echo "#12"
cat -n pupitar7 2>&1 | grep "ve"
echo " "

# удаление файлов и каталогов
echo "### 5 ###"
cd ..
rm -f cleffa5
rm lab0/shroomish8/blaziken/carracosta
chmod 300 lab0/magnemite0/zubat
chmod 300 lab0/magnemite0
rm -f lab0/magnemite0/zubat/bearticclef*
rm -f lab0/magnemite0/shelmetclef*
rm -rf magnemite0
chmod 600 lab0/magnemite0/vibrava
rm -rf lab0/magnemite0/vibrava/sandslash

# echo ""
# echo "errors: "
# cat /tmp/errors.log