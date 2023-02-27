<template>
    <div class="img-verify">
        <canvas ref="verify" :width="width" :height="height" @click="handleDraw"></canvas>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                pool: 'ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890',
                width: 140,
                height: 40,
                imgCode: ''
            }
        },

        methods: {
            init() {
                this.imgCode = this.draw()
            },

            handleDraw() {
                this.imgCode = this.draw()
            },

            randomNum(min, max) {
                return parseInt(Math.random() * (max - min) + min)
            },

            randomColor(min, max) {
                const r = this.randomNum(min, max)
                const g = this.randomNum(min, max)
                const b = this.randomNum(min, max)
                return `rgb(${r},${g},${b})`
            },

            draw() {
                const ctx = this.$refs.verify.getContext('2d')
                ctx.fillStyle = this.randomColor(180, 230)
                ctx.fillRect(0, 0, this.width, this.height)
                // 定义paramText
                let imgCode = ''
                for (let i = 0; i < 4; i++) {
                    const text = this.pool[this.randomNum(0, this.pool.length)]
                    imgCode += text
                    const fontSize = this.randomNum(18, 40)
                    const deg = this.randomNum(-30, 30)
                 
                    ctx.font = fontSize + 'px Simhei'
                    ctx.textBaseline = 'top'
                    ctx.fillStyle = this.randomColor(80, 150)
                  
                    ctx.save()
                    ctx.translate(30 * i + 15, 15)
                    ctx.rotate((deg * Math.PI) / 180)
                   
                    ctx.fillText(text, -15 + 5, -15)
                    ctx.restore()
                }

                for (let i = 0; i < 5; i++) {
                    ctx.beginPath()
                    ctx.moveTo(this.randomNum(0, this.width), this.randomNum(0, this.height))
                    ctx.lineTo(this.randomNum(0, this.width), this.randomNum(0, this.height))
                    ctx.strokeStyle = this.randomColor(180, 230)
                    ctx.closePath()
                    ctx.stroke()
                }

                for (let i = 0; i < 40; i++) {
                    ctx.beginPath()
                    ctx.arc(this.randomNum(0, this.width), this.randomNum(0, this.height), 1, 0, 2 * Math.PI)
                    ctx.closePath()
                    ctx.fillStyle = this.randomColor(150, 200)
                    ctx.fill()
                }
                return imgCode;
            }
        },
        mounted() {
            this.init()
        }
    }
</script>
<style>
    .img-verify canvas {
        cursor: pointer;
        border-radius: 18px;
    }
</style>